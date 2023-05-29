package com.crowdproj.rating.repo.tests

import com.crowdproj.rating.common.model.*

abstract class BaseInitRatings(val op: String): IInitObjects<CwpRating> {

    open val lockOld: CwpRatingLock = CwpRatingLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: CwpRatingLock = CwpRatingLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        scoreTypeId: CwpRatingScoreTypeId = CwpRatingScoreTypeId("1"),
        objectTypeId: CwpRatingObjectTypeId = CwpRatingObjectTypeId("11"),
        objectId: CwpRatingObjectId = CwpRatingObjectId("111"),
        ownerId: CwpRatingUserId = CwpRatingUserId("owner-123"),
        lock: CwpRatingLock = lockOld,
    ) = CwpRating(
        id = CwpRatingId("rating-repo-$op-$suf"),
        scoreTypeId = scoreTypeId,
        objectTypeId = objectTypeId,
        objectId = objectId,
        ownerId = ownerId,
        lock = lock,
    )
}