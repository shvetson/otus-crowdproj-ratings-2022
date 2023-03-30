package com.crowdproj.rating.common

import com.crowdproj.rating.common.model.*
import kotlinx.datetime.Instant

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 12:25
 */

data class CwpRatingContext(
    var command: CwpRatingCommand = CwpRatingCommand.NONE,
    var state: CwpRatingState = CwpRatingState.RUNNING,
    var errors: MutableList<CwpRatingError> = mutableListOf(),

    var workMode: CwpRatingWorkMode = CwpRatingWorkMode.PROD,
    var stubCase: CwpRatingStub = CwpRatingStub.NONE,

    var requestId: CwpRatingRequestId = CwpRatingRequestId.NONE,
    var timeStart: Instant = Instant.NONE,

    var ratingRequest: CwpRating = CwpRating(),
    var ratingFilterRequest: CwpRatingFilter = CwpRatingFilter(),
    var ratingResponse: CwpRating = CwpRating(),
    var ratingsResponse: MutableList<CwpRating> = mutableListOf(),
)