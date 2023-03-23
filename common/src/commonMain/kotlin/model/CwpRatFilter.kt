package com.crowdproj.rating.common.model

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 14:17
 */

data class CwpRatFilter(
    var searchString: String = "",
    var typeId: CwpRatId = CwpRatId.NONE,
    var objectId: CwpRatObjectId = CwpRatObjectId.NONE,
    var objectTypeId: CwpRatObjectTypeId = CwpRatObjectTypeId.NONE,
    var ownerId: CwpRatUserId = CwpRatUserId.NONE,
)