package com.crowdproj.rating.ktor.plugin

import com.crowdproj.rating.logging.common.CwpLoggerProvider
import com.crowdproj.rating.logging.logback.ratingLoggerLogback
import io.ktor.server.application.*

//actual fun Application.getLoggerProviderConf(): CwpLoggerProvider =
fun Application.getLoggerProviderConf(): CwpLoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
//        "kmp" -> CwpLoggerProvider { ratingLoggerKermit(it) }
        "logback", null -> CwpLoggerProvider { ratingLoggerLogback(it) }
        else -> throw Exception("Logger $mode is not allowed.")
}