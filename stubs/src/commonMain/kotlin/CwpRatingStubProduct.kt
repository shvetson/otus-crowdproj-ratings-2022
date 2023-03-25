package com.crowdproj.rating.stubs

import com.crowdproj.rating.common.NONE
import com.crowdproj.rating.common.model.*
import kotlinx.datetime.Instant

object CwpRatingStubProduct {
    val RATING_PRODUCT: CwpRating
        get() = CwpRating(
            id = CwpRatingId("1"),
            scoreTypeId = CwpRatingScoreTypeId("11"),
            objectId = CwpRatingObjectId("100"),
            objectTypeId = CwpRatingObjectTypeId("10"),
            score = 3.5,
            voteCount = 200,
            createdAt = Instant.parse("2010-06-01T22:19:44.475Z"),
            updatedAt = Instant.NONE,
            ownerId = CwpRatingUserId("1"),
            permissions = mutableListOf(
                CwpRatingPermission.READ,
                CwpRatingPermission.UPDATE,
                CwpRatingPermission.DELETE,
                CwpRatingPermission.MAKE_VISIBLE_PUBLIC,
                CwpRatingPermission.MAKE_VISIBLE_TO_GROUP,
                CwpRatingPermission.MAKE_VISIBLE_TO_OWNER,
            )
        )
}
