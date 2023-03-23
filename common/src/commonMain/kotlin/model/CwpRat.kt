package com.crowdproj.rating.common.model

import com.crowdproj.rating.common.NONE
import kotlinx.datetime.Instant

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 13:04
 */

// все должно быть изменяемым
data class CwpRat(
    var id: CwpRatId = CwpRatId.NONE,
    var scoreTypeId: CwpRatScoreTypeId = CwpRatScoreTypeId.NONE,
    var objectId: CwpRatObjectId = CwpRatObjectId.NONE,
    var objectTypeId: CwpRatObjectTypeId = CwpRatObjectTypeId.NONE,
    var score: String = "",
    var voteCount: String = "",
    var createdAt: Instant = Instant.NONE,
    var updatedAt: Instant = Instant.NONE,
    var ownerId: CwpRatUserId = CwpRatUserId.NONE,
    var permissions: MutableList<CwpRatPermission> = mutableListOf(),
)