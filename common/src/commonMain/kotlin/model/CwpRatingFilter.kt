package com.crowdproj.rating.common.model

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 14:17
 */

data class CwpRatingFilter(
    var searchString: String = "",
    var scoreTypeId: CwpRatingScoreTypeId = CwpRatingScoreTypeId.NONE,
    var objectTypeId: CwpRatingObjectTypeId = CwpRatingObjectTypeId.NONE,
    var objectId: CwpRatingObjectId = CwpRatingObjectId.NONE,
    var ownerId: CwpRatingUserId = CwpRatingUserId.NONE,
)