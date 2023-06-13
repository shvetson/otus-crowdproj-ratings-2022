package com.crowdproj.rating.ktor.plugin

import com.crowdproj.rating.ktor.CwpRatingAppSettings
import com.crowdproj.rating.ktor.controller.rating
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*

fun Application.configureRouting(appSettings: CwpRatingAppSettings) {
    routing {
        static("static") {
            resources("static")
        }
        swagger(appSettings)

        authenticate("auth-jwt") {
            rating(appSettings)
        }
    }
}