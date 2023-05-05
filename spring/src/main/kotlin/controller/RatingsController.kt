package com.crowdproj.rating.spring.controller

import com.crowdproj.rating.api.v1.models.*
import com.crowdproj.rating.common.model.CwpRatingCommand
import com.crowdproj.rating.logging.common.CwpLoggerProvider
import com.crowdproj.rating.mappers.*
import com.crowdproj.rating.spring.CwpRatingBlockingProcessor
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/ratings")
class RatingsController(
    private val loggerProvider: CwpLoggerProvider,
    private val processor: CwpRatingBlockingProcessor,
) {

    @PostMapping("/create")
    suspend fun createRating(@RequestBody request: RatingCreateRequest): RatingCreateResponse =
        processV1(
            processor,
            CwpRatingCommand.CREATE,
            request = request,
            loggerProvider.logger(RatingsController::class),
            "rating-create"
        )

    @PostMapping("/read")
    suspend fun readRating(@RequestBody request: RatingReadRequest): RatingReadResponse =
        processV1(
            processor,
            CwpRatingCommand.READ,
            request = request,
            loggerProvider.logger(RatingsController::class),
            "rating-read"
        )

    @PostMapping("/update")
    suspend fun updateRating(@RequestBody request: RatingUpdateRequest): RatingUpdateResponse =
        processV1(
            processor,
            CwpRatingCommand.UPDATE,
            request = request,
            loggerProvider.logger(RatingsController::class),
            "rating-update"
        )

    @PostMapping("/delete")
    suspend fun deleteRating(@RequestBody request: RatingDeleteRequest): RatingDeleteResponse =
        processV1(
            processor,
            CwpRatingCommand.DELETE,
            request = request,
            loggerProvider.logger(RatingsController::class),
            "rating-delete"
        )

    @PostMapping("/search")
    suspend fun searchRating(@RequestBody request: RatingSearchRequest): RatingSearchResponse =
        processV1(
            processor,
            CwpRatingCommand.SEARCH,
            request = request,
            loggerProvider.logger(RatingsController::class),
            "rating-search"
        )
}