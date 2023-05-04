package com.crowdproj.rating.ktor.plugin

import com.crowdproj.rating.ktor.CwpRatingAppSettings
import com.crowdproj.rating.logging.logback.CwpLogWrapperLogback
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.request.*
import kotlinx.coroutines.runBlocking
import org.slf4j.event.Level

fun Application.configureCallLogging(appSettings: CwpRatingAppSettings, clazz: String) {

    install(DoubleReceive)

    install(CallLogging) {
        level = Level.TRACE
        format { call ->
            runBlocking {
                "Body: ${call.receiveText()}"
            }
        }

        level = Level.INFO
        val lgr = appSettings
            .corSettings
            .loggerProvider
            .logger(clazz) as? CwpLogWrapperLogback
        lgr?.logger?.also { logger = it }
    }
}