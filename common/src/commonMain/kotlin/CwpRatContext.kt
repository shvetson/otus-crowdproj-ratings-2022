package com.crowdproj.rating.common

import com.crowdproj.rating.common.model.*
import kotlinx.datetime.Instant

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 12:25
 */

data class CwpRatContext(
    var command: CwpRatCommand = CwpRatCommand.NONE,
    var state: CwpRatState = CwpRatState.NONE,
    var errors: MutableList<CwpRatError> = mutableListOf(),

    var workMode: CwpRatWorkMode = CwpRatWorkMode.PROD,
    var stubCase: CwpRatStub = CwpRatStub.NONE,

    var requestId: CwpRatRequestId = CwpRatRequestId.NONE,
    var timeStart: Instant = Instant.NONE,

    var ratingRequest: CwpRat = CwpRat(),
    var ratingFilterRequest: CwpRatFilter = CwpRatFilter(),
    var ratingResponse: CwpRat = CwpRat(),
    var ratingsResponse: MutableList<CwpRat> = mutableListOf(),
)