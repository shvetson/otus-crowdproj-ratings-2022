package models

import NONE
import kotlinx.datetime.Instant

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 13:04
 */

data class MkplRating(
    var id: MkplRatingId = MkplRatingId.NONE,
    var typeId: MkplScoreTypeId = MkplScoreTypeId.NONE,
    var objectId: MkplObjectId = MkplObjectId.NONE,
    var objectType: MkplObjectType = MkplObjectType.NONE,
    var score: String = "",
    var voteCount: String = "",
    var createdAt: Instant = Instant.NONE,
    var updatedAt: Instant = Instant.NONE,
    var ownerId: MkplUserId = MkplUserId.NONE,
    var permissions: MutableList<MkplPermission> = mutableListOf(),
)