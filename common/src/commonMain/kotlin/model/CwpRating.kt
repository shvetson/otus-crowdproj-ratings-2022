package com.crowdproj.rating.common.model

import com.crowdproj.rating.common.NONE
import com.crowdproj.rating.common.permission.CwpRatingPrincipalRelations
import kotlinx.datetime.Instant

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 13:04
 */

// все должно быть изменяемым
data class CwpRating(
    var id: CwpRatingId = CwpRatingId.NONE,
    var scoreTypeId: CwpRatingScoreTypeId = CwpRatingScoreTypeId.NONE,
    var objectTypeId: CwpRatingObjectTypeId = CwpRatingObjectTypeId.NONE,
    var objectId: CwpRatingObjectId = CwpRatingObjectId.NONE,
    var score: Double = 0.0,
    var voteCount: Int = 0,
    var createdAt: Instant = Instant.NONE,
    var updatedAt: Instant = Instant.NONE,
    var ownerId: CwpRatingUserId = CwpRatingUserId.NONE,
    var visibility: CwpRatingVisibility = CwpRatingVisibility.NONE,
    var lock: CwpRatingLock = CwpRatingLock.NONE,
    var principalRelations: Set<CwpRatingPrincipalRelations> = emptySet(),
    var permissionsClient: MutableSet<CwpRatingPermission> = mutableSetOf(),
) {
    fun deepCopy(): CwpRating = copy(
        principalRelations = principalRelations.toSet(),
        permissionsClient = permissionsClient.toMutableSet(),
    )
    fun isEmpty() = this == NONE

    companion object {
        val NONE = CwpRating()
    }
}