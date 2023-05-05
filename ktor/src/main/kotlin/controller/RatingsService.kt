package com.crowdproj.rating.ktor.controller

import com.crowdproj.rating.api.v1.models.*
import com.crowdproj.rating.common.model.CwpRatingCommand
import com.crowdproj.rating.ktor.CwpRatingAppSettings
import com.crowdproj.rating.logging.common.ICwpLogWrapper
import io.ktor.server.application.*

suspend fun ApplicationCall.createRating(appSettings: CwpRatingAppSettings, logger: ICwpLogWrapper) =
    processV1<RatingCreateRequest, RatingCreateResponse>(appSettings, logger, "rating-create", CwpRatingCommand.CREATE)

suspend fun ApplicationCall.readRating(appSettings: CwpRatingAppSettings, logger: ICwpLogWrapper) =
    processV1<RatingReadRequest, RatingReadResponse>(appSettings, logger, "rating-read", CwpRatingCommand.READ)

suspend fun ApplicationCall.updateRating(appSettings: CwpRatingAppSettings, logger: ICwpLogWrapper) =
    processV1<RatingUpdateRequest, RatingUpdateResponse>(appSettings, logger, "rating-update", CwpRatingCommand.UPDATE)


suspend fun ApplicationCall.deleteRating(appSettings: CwpRatingAppSettings, logger: ICwpLogWrapper) =
    processV1<RatingDeleteRequest, RatingDeleteResponse>(appSettings, logger, "rating-delete", CwpRatingCommand.DELETE)

suspend fun ApplicationCall.searchRating(appSettings: CwpRatingAppSettings, logger: ICwpLogWrapper) =
    processV1<RatingSearchRequest, RatingSearchResponse>(appSettings, logger, "rating-search", CwpRatingCommand.SEARCH)