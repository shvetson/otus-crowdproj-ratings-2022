package com.crowdproj.rating.common.model

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 14:17
 */

data class CwpRatingFilter(
    var searchString: String = "",
    var typeId: CwpRatingId = CwpRatingId.NONE,
    var objectId: CwpRatingObjectId = CwpRatingObjectId.NONE,
    var objectTypeId: CwpRatingObjectTypeId = CwpRatingObjectTypeId.NONE,
    var ownerId: CwpRatingUserId = CwpRatingUserId.NONE,
)