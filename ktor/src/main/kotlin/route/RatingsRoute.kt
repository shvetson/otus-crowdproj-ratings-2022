package com.crowdproj.rating.ktor.route

import com.crowdproj.rating.ktor.CwpRatingAppSettings
import com.crowdproj.rating.ktor.controller.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.rating(appSettings: CwpRatingAppSettings) {
    route("/api/v1/ratings") {
        post("create") {
            call.createRating()
        }
        post("read") {
            call.readRating()
        }
        post("update") {
            call.updateRating()
        }
        post("delete") {
            call.deleteRating()
        }
        post("search") {
            call.searchRating()
        }
    }
}