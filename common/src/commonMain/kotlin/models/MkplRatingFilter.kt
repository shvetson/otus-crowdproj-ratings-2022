package models

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 14:17
 */

data class MkplRatingFilter(
    var searchString: String = "",
    var typeId: MkplRatingId = MkplRatingId.NONE,
    var objectId: MkplObjectId = MkplObjectId.NONE,
    var objectType: MkplObjectType = MkplObjectType.NONE,
    var ownerId: MkplUserId = MkplUserId.NONE,
)