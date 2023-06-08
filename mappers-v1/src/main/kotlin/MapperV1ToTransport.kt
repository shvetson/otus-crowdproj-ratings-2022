package com.crowdproj.rating.mappers

import com.crowdproj.rating.api.v1.models.*
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.NONE
import com.crowdproj.rating.common.exception.UnknownCommandException
import com.crowdproj.rating.common.model.*
import kotlinx.datetime.Instant

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 14:24
 */

// #1
fun CwpRatingContext.toTransport(): IResponse = when (command) {
    CwpRatingCommand.CREATE -> toTransportCreate()
    CwpRatingCommand.READ -> toTransportRead()
    CwpRatingCommand.UPDATE -> toTransportUpdate()
    CwpRatingCommand.DELETE -> toTransportDelete()
    CwpRatingCommand.SEARCH -> toTransportSearch()
    else -> throw UnknownCommandException(command)
}

// #2
fun CwpRatingContext.toTransportCreate() = RatingCreateResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == CwpRatingState.FINISHING && errors.isEmpty()) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    rating = ratingResponse.toTransportRating()
)

// #9
fun CwpRatingContext.toTransportRead() = RatingReadResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == CwpRatingState.FINISHING && errors.isEmpty()) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    rating = ratingResponse.toTransportRating()
)

// #10
fun CwpRatingContext.toTransportUpdate() = RatingUpdateResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == CwpRatingState.FINISHING && errors.isEmpty()) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    rating = ratingResponse.toTransportRating()
)

// #11
fun CwpRatingContext.toTransportDelete() = RatingDeleteResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == CwpRatingState.FINISHING && errors.isEmpty()) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    rating = ratingResponse.toTransportRating()
)

// #12
fun CwpRatingContext.toTransportSearch() = RatingSearchResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == CwpRatingState.FINISHING && errors.isEmpty()) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    ratings = ratingsResponse.toTransportRating()
)

// #13
fun List<CwpRating>.toTransportRating(): List<RatingResponseObject>? = this
    .map { it.toTransportRating() }
    .toList()
    .takeIf { it.isNotEmpty() }

// #3
fun MutableList<CwpRatingError>.toTransportErrors() = this
    .map { it.toTransportError() }
    .toList()
    .takeIf { it.isNotEmpty() }

// #4
fun CwpRatingError.toTransportError() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
)

// #5
fun CwpRating.toTransportRating() = RatingResponseObject(
    id = id.takeIf { it != CwpRatingId.NONE }?.asString(),
    scoreTypeId = scoreTypeId.takeIf { it != CwpRatingScoreTypeId.NONE }?.asString(),
    objectId = objectId.takeIf { it != CwpRatingObjectId.NONE }?.asString(),
    objectTypeId = objectTypeId.takeIf { it != CwpRatingObjectTypeId.NONE }?.asString(),
    score = score.toString(),
    voteCount = voteCount.toString(),
    createdAt = createdAt.takeIf { it != Instant.NONE }.toString(),
    updatedAt = updatedAt.takeIf { it != Instant.NONE }.toString(),
    ownerId = ownerId.takeIf { it != CwpRatingUserId.NONE }?.asString(),
    permissions = permissionsClient.toTransportPermissions()
)

// #7
fun MutableSet<CwpRatingPermission>.toTransportPermissions() = this
    .map { it.toTransportPermissions() }
    .toSet()
    .takeIf { it.isNotEmpty() }

// #8
fun CwpRatingPermission.toTransportPermissions() = when (this) {
    CwpRatingPermission.READ -> RatingPermissions.READ
    CwpRatingPermission.UPDATE -> RatingPermissions.UPDATE
    CwpRatingPermission.DELETE -> RatingPermissions.DELETE
    CwpRatingPermission.MAKE_VISIBLE_PUBLIC -> RatingPermissions.MAKE_VISIBLE_PUBLIC
    CwpRatingPermission.MAKE_VISIBLE_TO_GROUP -> RatingPermissions.MAKE_VISIBLE_GROUP
    CwpRatingPermission.MAKE_VISIBLE_TO_OWNER -> RatingPermissions.MAKE_VISIBLE_OWN
}