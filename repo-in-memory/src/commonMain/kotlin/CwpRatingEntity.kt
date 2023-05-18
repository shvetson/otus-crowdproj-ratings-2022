package com.crowdproj.rating.repo.inmemory

import com.crowdproj.rating.common.NONE
import com.crowdproj.rating.common.model.*
import kotlinx.datetime.Instant

data class CwpRatingEntity(
    val id: String? = null,
    val scoreTypeId: String? = null,
    val objectTypeId: String? = null,
    val objectId: String? = null,
    var score: Double? = null,
    var voteCount: Int? = null,
    var createdAt: Instant? = null,
    var updatedAt: Instant? = null,
    val ownerId: String? = null,
    val lock: String? = null,
) {
    constructor(model: CwpRating) : this(
        id = model.id.asString().takeIf { it != CwpRatingId.NONE.asString() },
        scoreTypeId = model.scoreTypeId.asString().takeIf { it != CwpRatingScoreTypeId.NONE.asString() },
        objectTypeId = model.objectTypeId.asString().takeIf { it != CwpRatingObjectTypeId.NONE.asString() },
        objectId = model.objectId.asString().takeIf { it != CwpRatingObjectId.NONE.asString() },
        score = model.score,
        voteCount = model.voteCount,
        createdAt = model.createdAt.takeIf { it != Instant.NONE },
        updatedAt = model.updatedAt.takeIf { it != Instant.NONE },
        ownerId = model.ownerId.asString().takeIf { it.isNotBlank() },
        lock = model.lock.asString().takeIf { it.isNotBlank() }
    )

    fun toInternal() = CwpRating(
        id = id?.let { CwpRatingId(it) } ?: CwpRatingId.NONE,
        scoreTypeId = scoreTypeId?.let { CwpRatingScoreTypeId(it) } ?: CwpRatingScoreTypeId.NONE,
        objectTypeId = objectTypeId?.let { CwpRatingObjectTypeId(it) } ?: CwpRatingObjectTypeId.NONE,
        objectId = objectId?.let { CwpRatingObjectId(it) } ?: CwpRatingObjectId.NONE,
        score = score ?: 0.0,
        voteCount = voteCount ?: 0,
        createdAt = createdAt ?: Instant.NONE,
        updatedAt = updatedAt ?: Instant.NONE,
        ownerId = ownerId?.let { CwpRatingUserId(it) } ?: CwpRatingUserId.NONE,
        lock = lock?.let { CwpRatingLock(it) } ?: CwpRatingLock.NONE,
    )
}