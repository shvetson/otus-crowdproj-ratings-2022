package com.crowdproj.rating.common

import com.crowdproj.rating.common.model.*
import com.crowdproj.rating.common.permission.CwpRatingPrincipalModel
import com.crowdproj.rating.common.permission.CwpRatingUserPermissions
import com.crowdproj.rating.common.repo.IRatingRepository
import com.crowdproj.rating.common.stub.CwpRatingStubs
import kotlinx.datetime.Instant

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 12:25
 */

data class CwpRatingContext(
    var command: CwpRatingCommand = CwpRatingCommand.NONE,
    var state: CwpRatingState = CwpRatingState.NONE,
    var errors: MutableList<CwpRatingError> = mutableListOf(),
    var settings: CwpRatingCorSettings = CwpRatingCorSettings.NONE,

    var workMode: CwpRatingWorkMode = CwpRatingWorkMode.STUB,
    var stubCase: CwpRatingStubs = CwpRatingStubs.NONE,

    var principal: CwpRatingPrincipalModel = CwpRatingPrincipalModel.NONE,
    val permissionsChain: MutableSet<CwpRatingUserPermissions> = mutableSetOf(),
    var permitted: Boolean = false,

    var requestId: CwpRatingRequestId = CwpRatingRequestId.NONE,
    var timeStart: Instant = Instant.NONE,

    var ratingRepo: IRatingRepository = IRatingRepository.NONE,
    var ratingRepoRead: CwpRating = CwpRating(),
    var ratingRepoPrepare: CwpRating = CwpRating(),
    var ratingRepoDone: CwpRating = CwpRating(),
    var ratingsRepoDone: MutableList<CwpRating> = mutableListOf(),

    var ratingRequest: CwpRating = CwpRating(),
    var ratingFilterRequest: CwpRatingFilter = CwpRatingFilter(),

    var ratingResponse: CwpRating = CwpRating(),
    var ratingsResponse: MutableList<CwpRating> = mutableListOf(),

    var ratingValidating: CwpRating = CwpRating(),
    var ratingFilterValidating: CwpRatingFilter = CwpRatingFilter(),

    var ratingValidated: CwpRating = CwpRating(),
    var ratingFilterValidated: CwpRatingFilter = CwpRatingFilter(),
)