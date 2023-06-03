package com.crowdproj.rating.mappers.log

import com.crowdproj.rating.api.log.models.*
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.*
import kotlinx.datetime.Clock

fun CwpRatingContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "cwp-ratings",
    rating = toCwpLog(),
    errors = errors.map { it.toLog() },
)

fun CwpRatingContext.toCwpLog(): CwpLogModel? {
    val ratingNone = CwpRating()
    return CwpLogModel(
        requestId = requestId.takeIf { it != CwpRatingRequestId.NONE }?.asString(),
        requestRating = ratingRequest.takeIf { it != ratingNone }?.toLog(),
        responseRating = ratingResponse.takeIf { it != ratingNone }?.toLog(),
        responseRatings = ratingsResponse.takeIf { it.isNotEmpty() }?.filter { it != ratingNone }?.map { it.toLog() },
        requestFilter = ratingFilterRequest.takeIf { it != CwpRatingFilter() }?.toLog(),
    ).takeIf { it != CwpLogModel() }
}

private fun CwpRatingFilter.toLog() = RatingFilterLog(
    searchString = searchString.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != CwpRatingUserId.NONE }?.asString(),
)

fun CwpRatingError.toLog() = ErrorLogModel(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
//    level = level.name,
)

fun CwpRating.toLog() = RatingLog(
    id = id.takeIf { it != CwpRatingId.NONE }?.asString(),
    scoreTypeId = scoreTypeId.takeIf { it != CwpRatingScoreTypeId.NONE }?.asString(),
    objectId = objectId.takeIf { it != CwpRatingObjectId.NONE }?.asString(),
    objectTypeId = objectTypeId.takeIf { it != CwpRatingObjectTypeId.NONE }?.asString(),
    ownerId = ownerId.takeIf { it != CwpRatingUserId.NONE }?.asString(),
    permissions = permissionsClient.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
)