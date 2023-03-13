import kotlinx.datetime.Instant
import models.*

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 12:25
 */

data class MkplContext(
    var command: MkplCommand = MkplCommand.NONE,
    var state: MkplState = MkplState.NONE,
    var errors: MutableList<MkplError> = mutableListOf(),

    var workMode: MkplWorkMode = MkplWorkMode.PROD,
    var stubCase: MkplStub = MkplStub.NONE,

    var requestId: MkplRequestId = MkplRequestId.NONE,
    var timeStart: Instant = Instant.NONE,

    var ratingRequest: MkplRating = MkplRating(),
    var ratingFilterRequest: MkplRatingFilter = MkplRatingFilter(),
    var ratingResponse: MkplRating = MkplRating(),
    var ratingsResponse: MutableList<MkplRating> = mutableListOf(),
)