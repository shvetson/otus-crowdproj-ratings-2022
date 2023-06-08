package com.crowdproj.rating.repo.postgresql

import com.crowdproj.rating.common.NONE
import com.crowdproj.rating.common.model.*
import kotlinx.datetime.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.dateTimeParam
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import java.sql.Date

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  16.05.2023 21:26
 */

object RatingTable : Table(name = "ratings") {
    val id = varchar("id", 128)
    val scoreTypeId = varchar("score_type_id", 128)
    val objectTypeId = varchar("object_type_id", 128)
    val objectId = varchar("object_id", 128)
    val score = double("score").nullable()
    val voteCount = integer("vote_count").nullable()
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at").nullable()
    val ownerId = varchar("owner_id", 128)
    val lock = varchar("lock", 128)

    override val primaryKey: PrimaryKey = PrimaryKey(id)

    fun fromRow(result: InsertStatement<Number>): CwpRating =
        CwpRating(
            id = CwpRatingId(result[id].toString()),
            scoreTypeId = CwpRatingScoreTypeId(result[scoreTypeId].toString()),
            objectTypeId = CwpRatingObjectTypeId(result[objectTypeId].toString()),
            objectId = CwpRatingObjectId(result[objectId].toString()),
            score = result[score] ?: 0.0,
            voteCount = result[voteCount] ?: 0,
            createdAt = result[createdAt].toKotlinLocalDateTime().toInstant(TimeZone.currentSystemDefault()),
            updatedAt = result[updatedAt]?.toKotlinLocalDateTime()?.toInstant(TimeZone.currentSystemDefault()) ?: Instant.NONE,
            ownerId = CwpRatingUserId(result[ownerId].toString()),
            lock = CwpRatingLock(result[lock])
        )

    fun fromRow(result: ResultRow): CwpRating =
        CwpRating(
            id = CwpRatingId(result[id].toString()),
            scoreTypeId = CwpRatingScoreTypeId(result[scoreTypeId].toString()),
            objectTypeId = CwpRatingObjectTypeId(result[objectTypeId].toString()),
            objectId = CwpRatingObjectId(result[objectId].toString()),
            score = result[score] ?: 0.0,
            voteCount = result[voteCount] ?: 0,
            createdAt = result[createdAt].toKotlinLocalDateTime().toInstant(TimeZone.currentSystemDefault()),
            updatedAt = result[updatedAt]?.toKotlinLocalDateTime()?.toInstant(TimeZone.currentSystemDefault()) ?: Instant.NONE,
            ownerId = CwpRatingUserId(result[ownerId].toString()),
            lock = CwpRatingLock(result[lock])
        )

    fun toRow(it: UpdateBuilder<*>, rating: CwpRating, randomUuid: () -> String) {
        it[id] = rating.id.takeIf { it != CwpRatingId.NONE }?.asString() ?: randomUuid()
        it[scoreTypeId] = rating.scoreTypeId.asString()
        it[objectTypeId] = rating.objectTypeId.asString()
        it[objectId] = rating.objectId.asString()
        it[score] = rating.score
        it[voteCount] = rating.voteCount
        it[createdAt] = (rating.createdAt.takeIf { it != Instant.NONE } ?: Clock.System.now()).toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
        it[updatedAt] = (rating.updatedAt.takeIf { it != Instant.NONE } ?: Clock.System.now()).toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
        it[ownerId] = rating.ownerId.asString()
        it[lock] = rating.lock.takeIf { it != CwpRatingLock.NONE }?.asString() ?: randomUuid()
    }
}