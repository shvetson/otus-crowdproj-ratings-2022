package com.crowdproj.rating.ktor.plugin

import com.crowdproj.rating.ktor.CwpRatingAppSettings
import com.crowdproj.rating.ktor.route.rating
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*

fun Application.configureRouting(appSettings: CwpRatingAppSettings) {
    routing {
        static("static") {
            resources("static")
        }
//        swagger(appSettings)

        rating(appSettings)
    }
}