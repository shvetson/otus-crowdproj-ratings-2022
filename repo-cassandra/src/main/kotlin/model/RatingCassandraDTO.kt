package com.crowdproj.rating.repo.cassandra.model

import com.crowdproj.rating.common.NONE
import com.crowdproj.rating.common.model.*
import com.datastax.oss.driver.api.core.type.DataTypes
import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import kotlinx.datetime.*

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  18.05.2023 14:05
 */

@Entity
data class RatingCassandraDTO(
    @field:PartitionKey
    @field:CqlName(COLUMN_ID)
    var id: String? = null,
    @field:CqlName(COLUMN_SCORE_TYPE_ID)
    var scoreTypeId: String? = null,
    @field:CqlName(COLUMN_OBJECT_TYPE_ID)
    var objectTypeId: String? = null,
    @field:CqlName(COLUMN_OBJECT_ID)
    var objectId: String? = null,
    @field:CqlName(COLUMN_SCORE)
    var score: Double = 0.0,
    @field:CqlName(COLUMN_VOTE_COUNT)
    var voteCount: Int = 0,
    @field:CqlName(COLUMN_CREATE_AT)
    var createdAt: String? = null,
    @field:CqlName(COLUMN_UPDATE_AT)
    var updatedAt: String? = null,
    @field:CqlName(COLUMN_OWNER_ID)
    var ownerId: String? = null,
    @field:CqlName(COLUMN_LOCK)
    var lock: String?,
) {

    constructor(ratingModel: CwpRating) : this(
        id = ratingModel.id.takeIf { it != CwpRatingId.NONE }?.asString(),
        scoreTypeId = ratingModel.scoreTypeId.takeIf { it != CwpRatingScoreTypeId.NONE }?.asString(),
        objectTypeId = ratingModel.objectTypeId.takeIf { it != CwpRatingObjectTypeId.NONE }?.asString(),
        objectId = ratingModel.objectId.takeIf { it != CwpRatingObjectId.NONE }?.asString(),
        score = ratingModel.score,
        voteCount = ratingModel.voteCount,
        createdAt = ratingModel.createdAt.takeIf { it != Instant.NONE }?.toString(),
        updatedAt = ratingModel.updatedAt.takeIf { it != Instant.NONE }?.toString(),
        ownerId = ratingModel.ownerId.takeIf { it != CwpRatingUserId.NONE }?.asString(),
        lock = ratingModel.lock.takeIf { it != CwpRatingLock.NONE }?.asString(),
    )

    fun toRatingModel(): CwpRating = CwpRating(
        id = id?.let { CwpRatingId(it) } ?: CwpRatingId.NONE,
        scoreTypeId = scoreTypeId?.let { CwpRatingScoreTypeId(it) } ?: CwpRatingScoreTypeId.NONE,
        objectTypeId = objectTypeId?.let { CwpRatingObjectTypeId(it) } ?: CwpRatingObjectTypeId.NONE,
        objectId = objectId?.let { CwpRatingObjectId(it) } ?: CwpRatingObjectId.NONE,
        score = score,
        voteCount = voteCount,
        createdAt = createdAt?.let { Instant.parse(it) } ?: Instant.NONE,
        updatedAt = updatedAt?.let { Instant.parse(it) } ?: Instant.NONE,
        ownerId = ownerId?.let { CwpRatingUserId(it) } ?: CwpRatingUserId.NONE,
        lock = lock?.let { CwpRatingLock(it) } ?: CwpRatingLock.NONE
    )

    companion object {
        const val TABLE_NAME = "ratings"

        const val COLUMN_ID = "id"
        const val COLUMN_SCORE_TYPE_ID = "scoreTypeId"
        const val COLUMN_OBJECT_TYPE_ID = "objectTypeId"
        const val COLUMN_OBJECT_ID = "objectId"
        const val COLUMN_SCORE = "score"
        const val COLUMN_VOTE_COUNT = "voteCount"
        const val COLUMN_CREATE_AT = "createdAt"
        const val COLUMN_UPDATE_AT = "updatedAt"
        const val COLUMN_OWNER_ID = "ownerId"
        const val COLUMN_LOCK = "lock"


        fun table(keyspace: String, tableName: String) =
            SchemaBuilder
                .createTable(keyspace, tableName)
                .ifNotExists()
                .withPartitionKey(COLUMN_ID, DataTypes.TEXT)
                .withColumn(COLUMN_SCORE_TYPE_ID, DataTypes.TEXT)
                .withColumn(COLUMN_OBJECT_TYPE_ID, DataTypes.TEXT)
                .withColumn(COLUMN_OBJECT_ID, DataTypes.TEXT)
                .withColumn(COLUMN_SCORE, DataTypes.DOUBLE)
                .withColumn(COLUMN_VOTE_COUNT, DataTypes.INT)
                .withColumn(COLUMN_CREATE_AT, DataTypes.TEXT)
                .withColumn(COLUMN_UPDATE_AT, DataTypes.TEXT)
                .withColumn(COLUMN_OWNER_ID, DataTypes.TEXT)
                .withColumn(COLUMN_LOCK, DataTypes.TEXT)
                .build()


//        fun scoreTypeIndex(keyspace: String, tableName: String, locale: String = "en") =
        fun scoreTypeIndex(keyspace: String, tableName: String,) =
            SchemaBuilder
                .createIndex()
                .ifNotExists()
                .usingSASI()
                .onTable(keyspace, tableName)
                .andColumn(COLUMN_SCORE_TYPE_ID)
//                .withSASIOptions(mapOf("mode" to "CONTAINS", "tokenization_locale" to locale ))
                .build()
    }
}