package com.crowdproj.rating.ktor.plugin

import com.crowdproj.rating.ktor.route.rating
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello, world!")
        }

        rating()
    }
}