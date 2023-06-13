package com.crowdproj.rating.repo.postgresql

import com.benasher44.uuid.uuid4
import com.crowdproj.rating.common.model.*
import com.crowdproj.rating.common.repo.*
import com.crowdproj.rating.repo.postgresql.RatingTable.fromRow
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  16.05.2023 22:48
 */

class RepoRatingSQL(
    properties: SqlProperties,
    initObjects: Collection<CwpRating> = emptyList(),
    private val randomUuid: () -> String = { uuid4().toString() },
) : IRatingRepository {

    init {
        val driver = when {
            properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
            else -> throw IllegalArgumentException("Unknown driver for url ${properties.url} ")
        }

        Database.connect(
            properties.url,
            driver,
            properties.user,
            properties.password
        )

        transaction {
            SchemaUtils.drop(RatingTable)
            SchemaUtils.create(RatingTable)
        }
    }

    override suspend fun createRating(rq: DbRatingRequest): DbRatingResponse = transaction {
        val result: CwpRating =
            RatingTable.insert { toRow(it, rq.rating, randomUuid) }
                .resultedValues?.singleOrNull()?.let { fromRow(it) } ?: return@transaction DbRatingResponse.errorSave
        DbRatingResponse.success(result)
    }

    override suspend fun readRating(rq: DbRatingIdRequest): DbRatingResponse = transaction {
        val result: CwpRating =
            RatingTable.select { RatingTable.id eq rq.id.asString() }
                .singleOrNull()?.let { fromRow(it) } ?: return@transaction DbRatingResponse.errorNotFound
        DbRatingResponse.success(result)
    }

    // без проверки на lock
    override suspend fun updateRating(rq: DbRatingRequest): DbRatingResponse = transaction {
        val id = rq.rating.id
        if (id == CwpRatingId.NONE) return@transaction DbRatingResponse.errorEmptyId
        val rating = rq.rating
        val result: Boolean =
            RatingTable.update({ RatingTable.id eq (id.asString()) }) { toRow(it, rating, randomUuid) } > 0
        DbRatingResponse(rating, result)
    }

    // без проверки на lock
    override suspend fun deleteRating(rq: DbRatingIdRequest): DbRatingResponse = transaction {
        val rating = runBlocking { readRating(rq).data } ?: return@transaction DbRatingResponse.errorEmptyId
        val result: Boolean =
            RatingTable.deleteWhere { RatingTable.id eq rq.id.asString() } > 0
        DbRatingResponse.success(rating, result)
    }

    override suspend fun searchRating(rq: DbRatingFilterRequest): DbRatingsResponse =
        transaction {
            val result = RatingTable.select {
                buildList {
                    add(Op.TRUE)
                    if (rq.scoreTypeId != CwpRatingScoreTypeId.NONE) {
                        add(RatingTable.scoreTypeId eq rq.scoreTypeId.asString())
                    }
                    if (rq.objectTypeId != CwpRatingObjectTypeId.NONE) {
                        add(RatingTable.objectTypeId eq rq.objectTypeId.asString())
                    }
                    if (rq.objectId != CwpRatingObjectId.NONE) {
                        add(RatingTable.objectId eq rq.objectId.asString())
                    }
                    if (rq.ownerId != CwpRatingUserId.NONE) {
                        add(RatingTable.ownerId eq rq.ownerId.asString())
                    }
                }.reduce { a, b -> a and b }
            }.map(::fromRow)

            DbRatingsResponse.success(result)
        }
}