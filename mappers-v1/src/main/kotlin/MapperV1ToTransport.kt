/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 18:27
 */

import com.crowdproj.marketplace.ratings.api.v1.models.*
import exception.UnknownCommandException
import kotlinx.datetime.Instant
import models.*

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 14:24
 */

// #1
fun MkplContext.toTransport(): IResponse = when (command) {
    MkplCommand.CREATE -> toTransportCreate()
    MkplCommand.READ -> toTransportRead()
    MkplCommand.UPDATE -> toTransportUpdate()
    MkplCommand.DELETE -> toTransportDelete()
    MkplCommand.SEARCH -> toTransportSearch()
    else -> throw UnknownCommandException(command)
}

// #2
fun MkplContext.toTransportCreate() = RatingCreateResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MkplState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    rating = ratingResponse.toTransportRating()
)

// #9
fun MkplContext.toTransportRead() = RatingReadResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MkplState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    rating = ratingResponse.toTransportRating()
)

// #10
fun MkplContext.toTransportUpdate() = RatingUpdateResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MkplState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    rating = ratingResponse.toTransportRating()
)

// #11
fun MkplContext.toTransportDelete() = RatingDeleteResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MkplState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    rating = ratingResponse.toTransportRating()
)

// #12
fun MkplContext.toTransportSearch() = RatingSearchResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MkplState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    ratings = ratingsResponse.toTransportRating()
)

// #13
fun List<MkplRating>.toTransportRating(): List<RatingResponseObject>? = this
    .map { it.toTransportRating() }
    .toList()
    .takeIf { it.isNotEmpty() }

// #3
fun MutableList<MkplError>.toTransportErrors() = this
    .map { it.toTransportError() }
    .toList()
    .takeIf { it.isNotEmpty() }

// #4
fun MkplError.toTransportError() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

// #5
fun MkplRating.toTransportRating() = RatingResponseObject(
    id = id.takeIf { it != MkplRatingId.NONE }?.asString(),
    typeId = typeId.takeIf { it != MkplScoreTypeId.NONE }?.asString(),
    objectId = objectId.takeIf { it != MkplObjectId.NONE }?.asString(),
    objectType = objectType.toTransportRating(),
    score = score,
    voteCount = voteCount,
    createdAt = createdAt.takeIf { it != Instant.NONE }.toString(),
    updatedAt = updatedAt.takeIf { it != Instant.NONE }.toString(),
    ownerId = ownerId.takeIf { it != MkplUserId.NONE }?.asString(),
    permissions = permissions.toTransportPermissions()
)

// #6
fun MkplObjectType.toTransportRating() = when (this) {
    MkplObjectType.AD -> ObjectType.AD
    MkplObjectType.COMMENT -> ObjectType.COMMENT
    MkplObjectType.PRODUCT -> ObjectType.PRODUCT
    MkplObjectType.NONE -> null
}

// #7
fun MutableList<MkplPermission>.toTransportPermissions() = this
    .map { it.toTransportPermissions() }
    .toSet()
    .takeIf { it.isNotEmpty() }

// #8
fun MkplPermission.toTransportPermissions() = when (this) {
    MkplPermission.READ -> RatingPermissions.READ
    MkplPermission.UPDATE -> RatingPermissions.UPDATE
    MkplPermission.DELETE -> RatingPermissions.DELETE
    MkplPermission.MAKE_VISIBLE_PUBLIC -> RatingPermissions.MAKE_VISIBLE_PUBLIC
    MkplPermission.MAKE_VISIBLE_TO_GROUP -> RatingPermissions.MAKE_VISIBLE_GROUP
    MkplPermission.MAKE_VISIBLE_TO_OWNER -> RatingPermissions.MAKE_VISIBLE_OWN
}