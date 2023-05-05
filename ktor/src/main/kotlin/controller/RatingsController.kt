@file:Suppress("DuplicatedCode")
package com.crowdproj.rating.ktor.controller

import com.crowdproj.rating.ktor.CwpRatingAppSettings
import com.crowdproj.rating.ktor.controller.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.rating(appSettings: CwpRatingAppSettings) {
    val loggerRating = appSettings.corSettings.loggerProvider.logger(Route::rating::class)

    route("/api/v1/ratings") {
        post("create") {
            call.createRating(appSettings, loggerRating)
        }
        post("read") {
            call.readRating(appSettings, loggerRating)
        }
        post("update") {
            call.updateRating(appSettings, loggerRating)
        }
        post("delete") {
            call.deleteRating(appSettings, loggerRating)
        }
        post("search") {
            call.searchRating(appSettings, loggerRating)
        }
    }
}