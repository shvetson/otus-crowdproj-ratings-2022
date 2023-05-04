package com.crowdproj.rating.ktor.controller

import com.crowdproj.rating.api.v1.models.*
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.mappers.*
import com.crowdproj.rating.stubs.CwpRatingStub
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

suspend fun ApplicationCall.createRating() {
    val request = receive<RatingCreateRequest>()
    val ctx = CwpRatingContext()
    ctx.fromTransport(request)
//    process(ctx)
    respond(ctx.toTransportCreate())
}

suspend fun ApplicationCall.readRating() {
    val request = receive<RatingReadRequest>()
    val context = CwpRatingContext()
    context.fromTransport(request)
    context.ratingResponse = CwpRatingStub.get()
    respond(context.toTransportRead())
}

suspend fun ApplicationCall.updateRating() {
    val request = receive<RatingUpdateRequest>()
    val context = CwpRatingContext()
    context.fromTransport(request)
    context.ratingResponse = CwpRatingStub.get()
    respond(context.toTransportUpdate())
}

suspend fun ApplicationCall.deleteRating() {
    val request = receive<RatingDeleteRequest>()
    val context = CwpRatingContext()
    context.fromTransport(request)
    context.ratingResponse = CwpRatingStub.get()
    respond(context.toTransportDelete())
}

suspend fun ApplicationCall.searchRating() {
    val request = receive<RatingSearchRequest>()
    val context = CwpRatingContext()
    context.fromTransport(request)
    context.ratingsResponse.addAll(CwpRatingStub.prepareSearchList("11"))
    respond(context.toTransportSearch())
}
