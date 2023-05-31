package com.crowdproj.rating.repo.gremlin

import com.benasher44.uuid.uuid4
import com.crowdproj.rating.common.helper.asCwpRatingError
import com.crowdproj.rating.common.helper.errorAdministration
import com.crowdproj.rating.common.helper.errorRepoConcurrency
import com.crowdproj.rating.common.model.*
import com.crowdproj.rating.common.repo.*
import com.crowdproj.rating.repo.gremlin.RatingGremlinConst.FIELD_LOCK
import com.crowdproj.rating.repo.gremlin.RatingGremlinConst.FIELD_OBJECT_ID
import com.crowdproj.rating.repo.gremlin.RatingGremlinConst.FIELD_OBJECT_TYPE_ID
import com.crowdproj.rating.repo.gremlin.RatingGremlinConst.FIELD_OWNER_ID
import com.crowdproj.rating.repo.gremlin.RatingGremlinConst.FIELD_SCORE_TYPE_ID
import com.crowdproj.rating.repo.gremlin.RatingGremlinConst.FIELD_TMP_RESULT
import com.crowdproj.rating.repo.gremlin.RatingGremlinConst.RESULT_LOCK_FAILURE
import com.crowdproj.rating.repo.gremlin.exception.DbDuplicatedElementsException
import com.crowdproj.rating.repo.gremlin.mapper.addCwpRating
import com.crowdproj.rating.repo.gremlin.mapper.label
import com.crowdproj.rating.repo.gremlin.mapper.listCwpRating
import com.crowdproj.rating.repo.gremlin.mapper.toCwpRating
import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.exception.ResponseException
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.`__` as gr

class RatingRepoGremlin(
    private val hosts: String,
    private val port: Int = 8182,
    private val enableSsl: Boolean = false,
    private val user: String = "root",
    private val pass: String = "root_root",
    initObjects: List<CwpRating> = emptyList(),
    initRepo: ((GraphTraversalSource) -> Unit)? = null,
    val randomUuid: () -> String = { uuid4().toString() },
) : IRatingRepository {

    val initializedObjects: List<CwpRating> // необходимо для получения id

    private val cluster by lazy {
        Cluster.build().apply {
            addContactPoints(*hosts.split(Regex("\\s*,\\s*")).toTypedArray())
            port(port)
            credentials(user, pass)
            enableSsl(enableSsl)
        }.create()
    }

    private val g by lazy { traversal().withRemote(DriverRemoteConnection.using(cluster)) } // соединение с бд

    init {
        if (initRepo != null) {
            initRepo(g)
        }
        initializedObjects = initObjects.map { save(it) }
    }

    private fun save(rating: CwpRating): CwpRating = g.addV(rating.label())
        .addCwpRating(rating)
        .listCwpRating()// получение обратного ответа
        .next()
        ?.toCwpRating()
        ?: throw RuntimeException("Cannot initialize object $rating")

    override suspend fun createRating(rq: DbRatingRequest): DbRatingResponse {
        val key = randomUuid()
        val rating = rq.rating.copy(id = CwpRatingId(key), lock = CwpRatingLock(randomUuid()))
        val dbRes = try {
            g.addV(rating.label())
                .addCwpRating(rating)
                .listCwpRating()
                .toList()
        } catch (e: Throwable) {
            if (e is ResponseException || e.cause is ResponseException) {
                return resultErrorNotFound(key)
            }
            return DbRatingResponse(
                data = null,
                isSuccess = false,
                errors = listOf(e.asCwpRatingError())
            )
        }

        // проверка дублирования объекта в бд
        return when (dbRes.size) {
            0 -> resultErrorNotFound(key)
            1 -> DbRatingResponse(
                data = dbRes.first().toCwpRating(),
                isSuccess = true,
            )

            else -> errorDuplication(key)
        }
    }

    override suspend fun readRating(rq: DbRatingIdRequest): DbRatingResponse {
        val key = rq.id.takeIf { it != CwpRatingId.NONE }?.asString() ?: return resultErrorEmptyId
        val dbRes = try {
            g.V(key).listCwpRating().toList()
        } catch (e: Throwable) {
            if (e is ResponseException || e.cause is ResponseException) {
                return resultErrorNotFound(key)
            }
            return DbRatingResponse(
                data = null,
                isSuccess = false,
                errors = listOf(e.asCwpRatingError())
            )
        }
        return when (dbRes.size) {
            0 -> resultErrorNotFound(key)
            1 -> DbRatingResponse(
                data = dbRes.first().toCwpRating(),
                isSuccess = true,
            )

            else -> errorDuplication(key)
        }
    }

    override suspend fun updateRating(rq: DbRatingRequest): DbRatingResponse {
        val key = rq.rating.id.takeIf { it != CwpRatingId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.rating.lock.takeIf { it != CwpRatingLock.NONE } ?: return resultErrorEmptyLock
        val newLock = CwpRatingLock(randomUuid())
        val newRating = rq.rating.copy(lock = newLock)
        val dbRes = try {
            g
                .V(key)
                .`as`("a")
                .choose(
                    gr.select<Vertex, Any>("a") // забираем сохраненный объект "a"
                        .values<String>(FIELD_LOCK) // вытаскиваем значение
                        .`is`(oldLock.asString()), // сравниваем с oldLock и получаем true или false
                    gr.select<Vertex, Vertex>("a").addCwpRating(newRating).listCwpRating(), // выполняется если true - заполняем новыми свойствами (addCwpRating) и забираем получившееся значение (listCwpRating)
                    gr.select<Vertex, Vertex>("a").listCwpRating(result = RESULT_LOCK_FAILURE) // выполняется если false - забираем объект, добавив в поле result, что lock failure
                )
                .toList()
        } catch (e: Throwable) {
            if (e is ResponseException || e.cause is ResponseException) {
                return resultErrorNotFound(key)
            }
            return DbRatingResponse(
                data = null,
                isSuccess = false,
                errors = listOf(e.asCwpRatingError())
            )
        }

        val ratingResult = dbRes.firstOrNull()?.toCwpRating() // проверяем, что получили - есть объект, или нет, блокировка совпала или нет
        return when {
            ratingResult == null -> resultErrorNotFound(key)
            dbRes.size > 1 -> errorDuplication(key)
            ratingResult.lock != newLock -> DbRatingResponse(
                data = ratingResult,
                isSuccess = false,
                errors = listOf(
                    errorRepoConcurrency(
                        expectedLock = oldLock,
                        actualLock = ratingResult.lock,
                    ),
                )
            )

            else -> DbRatingResponse(
                data = ratingResult,
                isSuccess = true,
            )
        }
    }

    override suspend fun deleteRating(rq: DbRatingIdRequest): DbRatingResponse {
        val key = rq.id.takeIf { it != CwpRatingId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.lock.takeIf { it != CwpRatingLock.NONE } ?: return resultErrorEmptyLock
        val dbRes = try {
            g
                .V(key)
                .`as`("a")
                .choose(
                    gr.select<Vertex, Vertex>("a")
                        .values<String>(FIELD_LOCK)
                        .`is`(oldLock.asString()),
                    gr.select<Vertex, Vertex>("a")
                        .sideEffect(gr.drop<Vertex>()) // операция drop выполняется над объектом потом (sideEffect), те отложенная
                        .listCwpRating(),
                    gr.select<Vertex, Vertex>("a")
                        .listCwpRating(result = RESULT_LOCK_FAILURE)
                )
                .toList()
        } catch (e: Throwable) {
            if (e is ResponseException || e.cause is ResponseException) {
                return resultErrorNotFound(key)
            }
            return DbRatingResponse(
                data = null,
                isSuccess = false,
                errors = listOf(e.asCwpRatingError())
            )
        }
        val dbFirst = dbRes.firstOrNull()
        val ratingResult = dbFirst?.toCwpRating()
        return when {
            ratingResult == null -> resultErrorNotFound(key)
            dbRes.size > 1 -> errorDuplication(key)
            dbFirst[FIELD_TMP_RESULT] == RESULT_LOCK_FAILURE -> DbRatingResponse(
                data = ratingResult,
                isSuccess = false,
                errors = listOf(
                    errorRepoConcurrency(
                        expectedLock = oldLock,
                        actualLock = ratingResult.lock,
                    ),
                )
            )

            else -> DbRatingResponse(
                data = ratingResult,
                isSuccess = true,
            )
        }
    }

    /**
     * Поиск объявлений по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchRating(rq: DbRatingFilterRequest): DbRatingsResponse {
        val result = try {
            g.V()
                .apply { rq.ownerId.takeIf { it != CwpRatingUserId.NONE }?.also { has(FIELD_OWNER_ID, it.asString()) } }
                .apply { rq.scoreTypeId.takeIf { it != CwpRatingScoreTypeId.NONE }?.also { has(FIELD_SCORE_TYPE_ID, it.asString()) } }
                .apply { rq.objectTypeId.takeIf { it != CwpRatingObjectTypeId.NONE }?.also { has(FIELD_OBJECT_TYPE_ID, it.asString()) } }
                .apply { rq.objectId.takeIf { it != CwpRatingObjectId.NONE }?.also { has(FIELD_OBJECT_ID, it.asString()) } }
                .listCwpRating()
                .toList()
        } catch (e: Throwable) {
            return DbRatingsResponse(
                isSuccess = false,
                data = null,
                errors = listOf(e.asCwpRatingError())
            )
        }
        return DbRatingsResponse(
            data = result.map { it.toCwpRating() },
            isSuccess = true
        )
    }

    companion object {
        val resultErrorEmptyId = DbRatingResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                CwpRatingError(
                    field = "id",
                    description = "Id must not be null or blank"
                )
            )
        )
        val resultErrorEmptyLock = DbRatingResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                CwpRatingError(
                    field = "lock",
                    description = "Lock must be provided"
                )
            )
        )

        fun resultErrorNotFound(key: String, e: Throwable? = null) = DbRatingResponse(
            isSuccess = false,
            data = null,
            errors = listOf(
                CwpRatingError(
                    code = "not-found",
                    field = "id",
                    description = "Not Found object with key $key",
                    exception = e
                )
            )
        )

        fun errorDuplication(key: String) = DbRatingResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                errorAdministration(
                    violationCode = "duplicateObjects",
                    description = "Database consistency failure",
                    exception = DbDuplicatedElementsException("Db contains multiple elements for id = '$key'")
                )
            )
        )
    }
}