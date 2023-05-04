package com.crowdproj.rating.spring.controller

import com.crowdproj.rating.api.v1.models.*
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.CwpRatingCorSettings
import com.crowdproj.rating.mappers.*
import com.crowdproj.rating.spring.service.CwpRatingBlockingProcessor
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/ratings")
class RatingsController(
    val corSettings: CwpRatingCorSettings,
    private val processor: CwpRatingBlockingProcessor
) {

    @PostMapping("/create")
    fun createRating(@RequestBody request: RatingCreateRequest): RatingCreateResponse {
        val ctx = CwpRatingContext()
        ctx.fromTransport(request)
        processor.exec(ctx)
        return ctx.toTransportCreate()
    }

    @PostMapping("/read")
    fun readRating(@RequestBody request: RatingReadRequest): RatingReadResponse {
        val ctx = CwpRatingContext()
        ctx.fromTransport(request)
        processor.exec(ctx)
        return ctx.toTransportRead()
    }

    @PostMapping("/update")
    fun updateRating(@RequestBody request: RatingUpdateRequest): RatingUpdateResponse {
        val ctx = CwpRatingContext()
        ctx.fromTransport(request)
        processor.exec(ctx)
        return ctx.toTransportUpdate()
    }

    @PostMapping("/delete")
    fun deleteRating(@RequestBody request: RatingDeleteRequest): RatingDeleteResponse {
        val ctx = CwpRatingContext()
        ctx.fromTransport(request)
        processor.exec(ctx)
        return ctx.toTransportDelete()
    }

    @PostMapping("/search")
    fun searchRating(@RequestBody request: RatingSearchRequest): RatingSearchResponse {
        val ctx = CwpRatingContext()
        ctx.fromTransport(request)
        processor.exec(ctx)
        return ctx.toTransportSearch()
    }
}