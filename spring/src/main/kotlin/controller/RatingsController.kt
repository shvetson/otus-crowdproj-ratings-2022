package com.crowdproj.rating.spring.controller

import com.crowdproj.rating.api.v1.models.*
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.mappers.*
import com.crowdproj.rating.stubs.CwpRatingStub
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/ratings")
class RatingsController {

    @PostMapping("/create")
    fun createRat(@RequestBody request: RatingCreateRequest): RatingCreateResponse {
        val context = CwpRatingContext()
        context.fromTransport(request)
        context.ratingResponse = CwpRatingStub.get()
        return context.toTransportCreate()
    }

    @PostMapping("/read")
    fun readRat(@RequestBody request: RatingReadRequest): RatingReadResponse {
        val context = CwpRatingContext()
        context.fromTransport(request)
        context.ratingResponse = CwpRatingStub.get()
        return context.toTransportRead()
    }

    @RequestMapping("/update", method = [RequestMethod.POST])
    fun updateRat(@RequestBody request: RatingUpdateRequest): RatingUpdateResponse {
        val context = CwpRatingContext()
        context.fromTransport(request)
        context.ratingResponse = CwpRatingStub.get()
        return context.toTransportUpdate()
    }

    @PostMapping("/delete")
    fun deleteRat(@RequestBody request: RatingDeleteRequest): RatingDeleteResponse {
        val context = CwpRatingContext()
        context.fromTransport(request)
        return context.toTransportDelete()
    }

    @PostMapping("/search")
    fun searchRat(@RequestBody request: RatingSearchRequest): RatingSearchResponse {
        val context = CwpRatingContext()
        context.fromTransport(request)
        context.ratingsResponse.add(CwpRatingStub.get())
        return context.toTransportSearch()
    }
}