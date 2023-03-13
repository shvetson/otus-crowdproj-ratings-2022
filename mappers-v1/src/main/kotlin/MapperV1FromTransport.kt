import com.crowdproj.marketplace.ratings.api.v1.models.*
import exception.UnknownRequestException
import kotlinx.datetime.Instant
import models.*

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 14:24
 */

// #1
fun MkplContext.fromTransport(request: IRequest) = when (request) {
    is RatingCreateRequest -> fromTransport(request)
    is RatingReadRequest -> fromTransport(request)
    is RatingUpdateRequest -> fromTransport(request)
    is RatingDeleteRequest -> fromTransport(request)
    is RatingSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestException(request::class)
}

// #3
fun IRequest?.requestId() = this?.requestId?.let { MkplRequestId(it) } ?: MkplRequestId.NONE

// #5
fun String?.toRatingId() = this?.let { MkplRatingId(it) } ?: MkplRatingId.NONE
fun String?.toTypeId() = this?.let { MkplScoreTypeId(it) } ?: MkplScoreTypeId.NONE
fun String?.toObjectId() = this?.let { MkplObjectId(it) } ?: MkplObjectId.NONE
fun String?.toUserId() = this?.let { MkplUserId(it) } ?: MkplUserId.NONE

// #10
fun String?.toRatingWithId() = MkplRating(id = this.toRatingId())

// #4
fun RatingCreateObject.toInternal() = MkplRating(
    typeId = this.typeId.toTypeId(),
    objectId = this.objectId.toObjectId(),
    objectType = this.objectType.fromTransport(),
    score = this.score ?: "",
    voteCount = this.voteCount ?: "",
    createdAt = (this.createdAt ?: Instant.NONE) as Instant,
    updatedAt = (this.updatedAt ?: Instant.NONE) as Instant,
    ownerId = this.ownerId.toUserId(),
)

// #14
fun RatingUpdateObject.toInternal() = MkplRating(
    id = this.id.toRatingId(),
    typeId = this.typeId.toTypeId(),
    objectId = this.objectId.toObjectId(),
    objectType = this.objectType.fromTransport(),
    score = this.score ?: "",
    voteCount = this.voteCount ?: "",
    createdAt = (this.createdAt ?: Instant.NONE) as Instant,
    updatedAt = (this.updatedAt ?: Instant.NONE) as Instant,
    ownerId = this.ownerId.toUserId(),
)

// #15
fun RatingSearchFilter?.toInternal() = MkplRatingFilter(
    searchString = this?.searchString ?: ""
)

// #7
fun RatingDebug?.transportToWorkMode() = when (this?.mode) {
    RatingRequestDebugMode.PROD -> MkplWorkMode.PROD
    RatingRequestDebugMode.STUB -> MkplWorkMode.STUB
    RatingRequestDebugMode.TEST -> MkplWorkMode.TEST
    null -> MkplWorkMode.PROD
}

// #8
fun RatingDebug?.transportToStubCase() = when (this?.stub) {
    RatingRequestDebugStubs.SUCCESS -> MkplStub.SUCCESS
    RatingRequestDebugStubs.NOT_FOUND -> MkplStub.NOT_FOUND
    RatingRequestDebugStubs.BAD_ID -> MkplStub.BAD_ID
    RatingRequestDebugStubs.BAD_OBJECT_ID -> MkplStub.BAD_OBJECT_ID
    RatingRequestDebugStubs.CANNOT_DELETE -> MkplStub.CANNOT_DELETE
    RatingRequestDebugStubs.BAD_SEARCH_STRING -> MkplStub.BAD_SEARCH_STRING
    null -> MkplStub.NONE
}

// #2
fun MkplContext.fromTransport(request: RatingCreateRequest) {
    command = MkplCommand.CREATE
    requestId = request.requestId()
    ratingRequest = request.rating?.toInternal() ?: MkplRating()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

// #11
fun MkplContext.fromTransport(request: RatingReadRequest) {
    command = MkplCommand.READ
    requestId = request.requestId()
    ratingRequest = request.rating?.id.toRatingWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

// #12
fun MkplContext.fromTransport(request: RatingUpdateRequest) {
    command = MkplCommand.UPDATE
    requestId = request.requestId()
    ratingRequest = request.rating?.toInternal() ?: MkplRating()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

// #9
fun MkplContext.fromTransport(request: RatingDeleteRequest) {
    command = MkplCommand.DELETE
    requestId = request.requestId()
    ratingRequest = request.rating?.id.toRatingWithId() // когда rating приходит не как объект, а как id
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

// #14
fun MkplContext.fromTransport(request: RatingSearchRequest) {
    command = MkplCommand.SEARCH
    requestId = request.requestId()
    ratingFilterRequest = request.ratingFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

// #6
fun ObjectType?.fromTransport() = when (this) {
    ObjectType.AD -> MkplObjectType.AD
    ObjectType.COMMENT -> MkplObjectType.COMMENT
    ObjectType.PRODUCT -> MkplObjectType.PRODUCT
    null -> MkplObjectType.NONE
}