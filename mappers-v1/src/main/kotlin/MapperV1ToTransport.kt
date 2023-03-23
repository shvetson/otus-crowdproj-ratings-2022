package com.crowdproj.rating.mappers

import com.crowdproj.rating.api.v1.models.*
import com.crowdproj.rating.common.CwpRatContext
import com.crowdproj.rating.common.NONE
import com.crowdproj.rating.common.exception.UnknownCommandException
import com.crowdproj.rating.common.model.*
import kotlinx.datetime.Instant
import kotlin.Error

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 14:24
 */

// #1
fun CwpRatContext.toTransport(): IResponse = when (command) {
    CwpRatCommand.CREATE -> toTransportCreate()
    CwpRatCommand.READ -> toTransportRead()
    CwpRatCommand.UPDATE -> toTransportUpdate()
    CwpRatCommand.DELETE -> toTransportDelete()
    CwpRatCommand.SEARCH -> toTransportSearch()
    else -> throw UnknownCommandException(command)
}

// #2
fun CwpRatContext.toTransportCreate() = RatingCreateResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == CwpRatState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    rating = ratingResponse.toTransportRating()
)

// #9
fun CwpRatContext.toTransportRead() = RatingReadResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == CwpRatState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    rating = ratingResponse.toTransportRating()
)

// #10
fun CwpRatContext.toTransportUpdate() = RatingUpdateResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == CwpRatState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    rating = ratingResponse.toTransportRating()
)

// #11
fun CwpRatContext.toTransportDelete() = RatingDeleteResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == CwpRatState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    rating = ratingResponse.toTransportRating()
)

// #12
fun CwpRatContext.toTransportSearch() = RatingSearchResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == CwpRatState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    ratings = ratingsResponse.toTransportRating()
)

// #13
fun List<CwpRat>.toTransportRating(): List<RatingResponseObject>? = this
    .map { it.toTransportRating() }
    .toList()
    .takeIf { it.isNotEmpty() }

// #3
fun MutableList<CwpRatError>.toTransportErrors() = this
    .map { it.toTransportError() }
    .toList()
    .takeIf { it.isNotEmpty() }

// #4
fun CwpRatError.toTransportError() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
)

// #5
fun CwpRat.toTransportRating() = RatingResponseObject(
    id = id.takeIf { it != CwpRatId.NONE }?.asString(),
    scoreTypeId = scoreTypeId.takeIf { it != CwpRatScoreTypeId.NONE }?.asString(),
    objectId = objectId.takeIf { it != CwpRatObjectId.NONE }?.asString(),
    objectTypeId = objectTypeId.takeIf { it != CwpRatObjectTypeId.NONE }?.asString(),
    score = score,
    voteCount = voteCount,
    createdAt = createdAt.takeIf { it != Instant.NONE }.toString(),
    updatedAt = updatedAt.takeIf { it != Instant.NONE }.toString(),
    ownerId = ownerId.takeIf { it != CwpRatUserId.NONE }?.asString(),
    permissions = permissions.toTransportPermissions()
)

// #7
fun MutableList<CwpRatPermission>.toTransportPermissions() = this
    .map { it.toTransportPermissions() }
    .toSet()
    .takeIf { it.isNotEmpty() }

// #8
fun CwpRatPermission.toTransportPermissions() = when (this) {
    CwpRatPermission.READ -> RatingPermissions.READ
    CwpRatPermission.UPDATE -> RatingPermissions.UPDATE
    CwpRatPermission.DELETE -> RatingPermissions.DELETE
    CwpRatPermission.MAKE_VISIBLE_PUBLIC -> RatingPermissions.MAKE_VISIBLE_PUBLIC
    CwpRatPermission.MAKE_VISIBLE_TO_GROUP -> RatingPermissions.MAKE_VISIBLE_GROUP
    CwpRatPermission.MAKE_VISIBLE_TO_OWNER -> RatingPermissions.MAKE_VISIBLE_OWN
}