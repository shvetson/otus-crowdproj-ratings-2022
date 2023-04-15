package com.crowdproj.rating.ktor.plugin

import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.request.*
import kotlinx.coroutines.runBlocking
import org.slf4j.event.Level

fun Application.configureCallLogging() {

    install(DoubleReceive)

    install(CallLogging) {
        level = Level.TRACE
        format { call ->
            runBlocking {
                "Body: ${call.receiveText()}"
            }
        }
    }
}