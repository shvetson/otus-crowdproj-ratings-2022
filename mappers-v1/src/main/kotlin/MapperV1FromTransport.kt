package com.crowdproj.rating.mappers

import com.crowdproj.rating.api.v1.models.*
import com.crowdproj.rating.common.CwpRatContext
import com.crowdproj.rating.common.exception.UnknownRequestException
import com.crowdproj.rating.common.model.*

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 14:24
 */

// #1
fun CwpRatContext.fromTransport(request: IRequest) = when (request) {
    is RatingCreateRequest -> fromTransport(request)
    is RatingReadRequest -> fromTransport(request)
    is RatingUpdateRequest -> fromTransport(request)
    is RatingDeleteRequest -> fromTransport(request)
    is RatingSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestException(request::class)
}

// #3
fun IRequest?.requestId() = this?.requestId?.let { CwpRatRequestId(it) } ?: CwpRatRequestId.NONE

// #5
fun String?.toRatingId() = this?.let { CwpRatId(it) } ?: CwpRatId.NONE
fun String?.toScoreTypeId() = this?.let { CwpRatScoreTypeId(it) } ?: CwpRatScoreTypeId.NONE
fun String?.toObjectTypeId() = this?.let { CwpRatObjectTypeId(it) } ?: CwpRatObjectTypeId.NONE

// #
fun String?.toObjectId() = this?.let { CwpRatObjectId(it) } ?: CwpRatObjectId.NONE

// #9
fun String?.toRatingWithId() = CwpRat(id = this.toRatingId())

// #6
fun RatingDebug?.transportToWorkMode() = when (this?.mode) {
    RatingRequestDebugMode.PROD -> CwpRatWorkMode.PROD
    RatingRequestDebugMode.STUB -> CwpRatWorkMode.STUB
    RatingRequestDebugMode.TEST -> CwpRatWorkMode.TEST
    null -> CwpRatWorkMode.PROD
}

// #7
fun RatingDebug?.transportToStubCase() = when (this?.stub) {
    RatingRequestDebugStubs.SUCCESS -> CwpRatStub.SUCCESS
    RatingRequestDebugStubs.NOT_FOUND -> CwpRatStub.NOT_FOUND
    RatingRequestDebugStubs.BAD_ID -> CwpRatStub.BAD_ID
    null -> CwpRatStub.NONE
}

// #4
fun RatingCreateObject.toInternal() = CwpRat(
    scoreTypeId = this.scoreTypeId.toScoreTypeId(), // -> #5
    objectId = this.objectId.toObjectId(), // -> #5
    objectTypeId = this.objectTypeId.toObjectTypeId(), // -> #5
)

// #14
fun RatingUpdateObject.toInternal() = CwpRat(
    id = this.id.toRatingId(),
    scoreTypeId = this.scoreTypeId.toScoreTypeId(),
    objectId = this.objectId.toObjectId(), // -> #
    objectTypeId = this.objectTypeId.toObjectTypeId(),
    score = this.score ?: "",
    voteCount = this.voteCount ?: "",
)

// #15
fun RatingSearchFilter?.toInternal() = CwpRatFilter(
    searchString = this?.searchString ?: ""
)

// #2
fun CwpRatContext.fromTransport(request: RatingCreateRequest) {
    command = CwpRatCommand.CREATE
    requestId = request.requestId() // -> #3
    ratingRequest = request.rating?.toInternal() ?: CwpRat() // -> #4
    workMode = request.debug.transportToWorkMode() // -> #6
    stubCase = request.debug.transportToStubCase() // -> #7
}

// #11
fun CwpRatContext.fromTransport(request: RatingReadRequest) {
    command = CwpRatCommand.READ
    requestId = request.requestId()
    ratingRequest = request.rating?.id.toRatingWithId() // -> #9
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

// #12
fun CwpRatContext.fromTransport(request: RatingUpdateRequest) {
    command = CwpRatCommand.UPDATE
    requestId = request.requestId()
    ratingRequest = request.rating?.toInternal() ?: CwpRat()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

// #8
fun CwpRatContext.fromTransport(request: RatingDeleteRequest) {
    command = CwpRatCommand.DELETE
    requestId = request.requestId()
    ratingRequest = request.rating?.id.toRatingWithId() // -> #9 (когда rating приходит не как объект, а как id)
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

// #14
fun CwpRatContext.fromTransport(request: RatingSearchRequest) {
    command = CwpRatCommand.SEARCH
    requestId = request.requestId()
    ratingFilterRequest = request.ratingFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}