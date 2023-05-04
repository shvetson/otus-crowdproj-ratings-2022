package com.crowdproj.rating.ktor.plugin

import com.crowdproj.rating.biz.CwpRatingProcessor
import com.crowdproj.rating.common.CwpRatingCorSettings
import com.crowdproj.rating.ktor.CwpRatingAppSettings
import com.crowdproj.rating.logging.common.CwpLoggerProvider
import io.ktor.server.application.*

fun Application.initAppSettings(): CwpRatingAppSettings = CwpRatingAppSettings(
    appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
    corSettings = CwpRatingCorSettings(loggerProvider = getLoggerProviderConf()),
    processor = CwpRatingProcessor(),
)

expect fun Application.getLoggerProviderConf(): CwpLoggerProvider