package com.crowdproj.rating.mappers

import com.crowdproj.rating.api.v1.models.*
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.exception.UnknownRequestException
import com.crowdproj.rating.common.model.*
import com.crowdproj.rating.common.stub.CwpRatingStub

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 14:24
 */

// #1
fun CwpRatingContext.fromTransport(request: IRequest) = when (request) {
    is RatingCreateRequest -> fromTransport(request)
    is RatingReadRequest -> fromTransport(request)
    is RatingUpdateRequest -> fromTransport(request)
    is RatingDeleteRequest -> fromTransport(request)
    is RatingSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestException(request::class)
}

// #3
fun IRequest?.requestId() = this?.requestId?.let { CwpRatingRequestId(it) } ?: CwpRatingRequestId.NONE

// #5
fun String?.toRatingId() = this?.let { CwpRatingId(it) } ?: CwpRatingId.NONE
fun String?.toScoreTypeId() = this?.let { CwpRatingScoreTypeId(it) } ?: CwpRatingScoreTypeId.NONE
fun String?.toObjectTypeId() = this?.let { CwpRatingObjectTypeId(it) } ?: CwpRatingObjectTypeId.NONE

// #
fun String?.toObjectId() = this?.let { CwpRatingObjectId(it) } ?: CwpRatingObjectId.NONE

// #9
fun String?.toRatingWithId() = CwpRating(id = this.toRatingId())

// #6
fun RatingDebug?.transportToWorkMode() = when (this?.mode) {
    RatingRequestDebugMode.PROD -> CwpRatingWorkMode.PROD
    RatingRequestDebugMode.STUB -> CwpRatingWorkMode.STUB
    RatingRequestDebugMode.TEST -> CwpRatingWorkMode.TEST
    null -> CwpRatingWorkMode.PROD
}

// #7
fun RatingDebug?.transportToStubCase() = when (this?.stub) {
    RatingRequestDebugStubs.SUCCESS -> CwpRatingStub.SUCCESS
    RatingRequestDebugStubs.NOT_FOUND -> CwpRatingStub.NOT_FOUND
    RatingRequestDebugStubs.BAD_ID -> CwpRatingStub.BAD_ID
    null -> CwpRatingStub.NONE
}

// #4
fun RatingCreateObject.toInternal() = CwpRating(
    scoreTypeId = this.scoreTypeId.toScoreTypeId(), // -> #5
    objectId = this.objectId.toObjectId(), // -> #5
    objectTypeId = this.objectTypeId.toObjectTypeId(), // -> #5
)

// #14
fun RatingUpdateObject.toInternal() = CwpRating(
    id = this.id.toRatingId(),
    scoreTypeId = this.scoreTypeId.toScoreTypeId(),
    objectId = this.objectId.toObjectId(), // -> #
    objectTypeId = this.objectTypeId.toObjectTypeId(),
)

// #15
fun RatingSearchFilter?.toInternal() = CwpRatingFilter(
    searchString = this?.searchString ?: ""
)

// #2
fun CwpRatingContext.fromTransport(request: RatingCreateRequest) {
    command = CwpRatingCommand.CREATE
    state = CwpRatingState.NONE
    requestId = request.requestId() // -> #3
    ratingRequest = request.rating?.toInternal() ?: CwpRating() // -> #4
    workMode = request.debug.transportToWorkMode() // -> #6
    stubCase = request.debug.transportToStubCase() // -> #7
}

// #11
fun CwpRatingContext.fromTransport(request: RatingReadRequest) {
    command = CwpRatingCommand.READ
    state = CwpRatingState.NONE
    requestId = request.requestId()
    ratingRequest = request.rating?.id.toRatingWithId() // -> #9
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

// #12
fun CwpRatingContext.fromTransport(request: RatingUpdateRequest) {
    command = CwpRatingCommand.UPDATE
    state = CwpRatingState.NONE
    requestId = request.requestId()
    ratingRequest = request.rating?.toInternal() ?: CwpRating()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

// #8
fun CwpRatingContext.fromTransport(request: RatingDeleteRequest) {
    command = CwpRatingCommand.DELETE
    state = CwpRatingState.NONE
    requestId = request.requestId()
    ratingRequest = request.rating?.id.toRatingWithId() // -> #9 (когда rating приходит не как объект, а как id)
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

// #14
fun CwpRatingContext.fromTransport(request: RatingSearchRequest) {
    command = CwpRatingCommand.SEARCH
    state = CwpRatingState.NONE
    requestId = request.requestId()
    ratingFilterRequest = request.ratingFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}