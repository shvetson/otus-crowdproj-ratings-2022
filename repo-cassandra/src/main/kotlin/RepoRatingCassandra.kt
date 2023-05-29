package com.crowdproj.rating.repo.cassandra

import com.benasher44.uuid.uuid4
import com.crowdproj.rating.common.helper.asCwpRatingError
import com.crowdproj.rating.common.model.CwpRating
import com.crowdproj.rating.common.model.CwpRatingError
import com.crowdproj.rating.common.model.CwpRatingId
import com.crowdproj.rating.common.model.CwpRatingLock
import com.crowdproj.rating.common.repo.*
import com.crowdproj.rating.repo.cassandra.model.RatingCassandraDTO
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.slf4j.LoggerFactory
import java.net.InetAddress
import java.net.InetSocketAddress
import java.util.concurrent.CompletionStage
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  18.05.2023 17:03
 */

class RepoRatingCassandra(
    private val keyspaceName: String,
    private val host: String = "",
    private val port: Int = 9042,
    private val user: String = "cassandra",
    private val pass: String = "cassandra",
    private val testing: Boolean = false,
    private val timeout: Duration = 30.toDuration(DurationUnit.SECONDS), // для тестирования
    private val randomUuid: () -> String = { uuid4().toString() },
    initObjects: Collection<CwpRating> = emptyList(),
) : IRatingRepository {
    private val log = LoggerFactory.getLogger(javaClass)

    // регистрация enum в cassandra

    private val session by lazy {
        CqlSession.builder()
            .addContactPoints(parseAddresses(host, port))
            .withLocalDatacenter("datacenter1")
            .withAuthCredentials(user, pass)
//            .withCodecRegistry()
            .build()
    }

    private val mapper by lazy { CassandraMapper.builder(session).build() }

    private fun createSchema(keyspaceName: String) {
        session.execute(
            SchemaBuilder.createKeyspace(keyspaceName)
                .ifNotExists().withSimpleStrategy(1)
                .build()
        )
        session.execute(RatingCassandraDTO.table(keyspaceName, RatingCassandraDTO.TABLE_NAME))
        session.execute(RatingCassandraDTO.scoreTypeIndex(keyspaceName, RatingCassandraDTO.TABLE_NAME))
    }

    private val dao by lazy {
        if (testing) {
            createSchema(keyspaceName)
        }
        mapper.ratingDao(keyspaceName, RatingCassandraDTO.TABLE_NAME).apply {
            runBlocking {

//                CompletableFutures
//                    .allDone(initObjects.map { this.create(RatingCassandraDTO(it)) })
//                    .toCompletableFuture()
//                    .get()

                initObjects.forEach { rating ->
                    withTimeout(timeout) {
                        create(RatingCassandraDTO(rating)).await()
                    }
                }
            }
        }
    }

    private fun errorToRatingResponse(e: Exception) = DbRatingResponse.error(e.asCwpRatingError())
    private fun errorToRatingsResponse(e: Exception) = DbRatingsResponse.error(e.asCwpRatingError())

    // обертка на запись
    private suspend inline fun <DbRes, Response> doDbAction(
        name: String,
        crossinline daoAction: () -> CompletionStage<DbRes>,
        okToResponse: (DbRes) -> Response,
        errorToResponse: (Exception) -> Response,
    ): Response = doDbAction(
        name,
        {
            val dbRes = withTimeout(timeout) { daoAction().await() }
            okToResponse(dbRes)

        },
        errorToResponse
    )

    private inline fun <Response> doDbAction(
        name: String,
        daoAction: () -> Response,
        errorToResponse: (Exception) -> Response,
    ): Response =
        try {
            daoAction()
        } catch (e: Exception) {
            log.error("Failed to $name", e)
            errorToResponse(e)
        }

    // обертка на чтение
    private suspend inline fun readAndDoDbAction(
        name: String,
        id: CwpRatingId,
        successResult: CwpRating?,
        daoAction: () -> CompletionStage<Boolean>,
        errorToResponse: (Exception) -> DbRatingResponse,
    ): DbRatingResponse =
        if (id == CwpRatingId.NONE)
            ID_IS_EMPTY
        else doDbAction(
            name,
            {
                val read = dao.read(id.asString()).await()
                if (read == null) ID_NOT_FOUND
                else {
                    val success = daoAction().await()
                    if (success) DbRatingResponse.success(successResult ?: read.toRatingModel())
                    else DbRatingResponse(
                        read.toRatingModel(),
                        false,
                        CONCURRENT_MODIFICATION.errors
                    )
                }
            },
            errorToResponse
        )

    override suspend fun createRating(rq: DbRatingRequest): DbRatingResponse {
        val new = rq.rating.copy(id = CwpRatingId(randomUuid()), lock = CwpRatingLock(randomUuid()))
        return doDbAction(
            "create",
            { dao.create(RatingCassandraDTO(new)) },
            { DbRatingResponse.success(new) },
            ::errorToRatingResponse
        )
    }

    override suspend fun readRating(rq: DbRatingIdRequest): DbRatingResponse {
        return if (rq.id == CwpRatingId.NONE) ID_IS_EMPTY
        else doDbAction(
            "read",
            { dao.read(rq.id.asString()) },
            { found ->
                if (found != null) {
                    DbRatingResponse.success(found.toRatingModel())
                } else {
                    ID_NOT_FOUND
                }
            },
            ::errorToRatingResponse
        )
    }

    override suspend fun updateRating(rq: DbRatingRequest): DbRatingResponse {
        val prevLock = rq.rating.lock.asString()
        val new = rq.rating.copy(lock = CwpRatingLock(randomUuid()))
        val dto = RatingCassandraDTO(new)

        return readAndDoDbAction(
            "update",
            rq.rating.id,
            new,
            { dao.update(dto, prevLock) },
            ::errorToRatingResponse
        )
    }

    override suspend fun deleteRating(rq: DbRatingIdRequest): DbRatingResponse {
        return readAndDoDbAction(
            "delete",
            rq.id,
            null,
            { dao.delete(rq.id.asString(), rq.lock.asString()) },
            ::errorToRatingResponse
        )
    }

    override suspend fun searchRating(rq: DbRatingFilterRequest): DbRatingsResponse {
        return doDbAction(
            "search",
            { dao.search(rq) },
            { found ->
                DbRatingsResponse.success(found.map { it.toRatingModel() }) },
            ::errorToRatingsResponse
        )
    }

    companion object {
        private val ID_IS_EMPTY = DbRatingResponse.error(CwpRatingError(field = "id", description = "id is empty"))
        private val ID_NOT_FOUND =
            DbRatingResponse.error(CwpRatingError(field = "id", code = "not-found", description = "not found"))
        private val CONCURRENT_MODIFICATION = DbRatingResponse.error(
            CwpRatingError(
                field = "lock",
                code = "concurrency",
                description = "concurrent modification"
            )
        )
    }
}

private fun parseAddresses(hosts: String, port: Int): Collection<InetSocketAddress> = hosts
    .split(Regex("""\s*,\s*"""))
    .map { InetSocketAddress(InetAddress.getByName(it), port) }