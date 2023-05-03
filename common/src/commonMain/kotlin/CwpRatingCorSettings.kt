package com.crowdproj.rating.common

import com.crowdproj.rating.logging.common.CwpLoggerProvider

data class CwpRatingCorSettings(
    val loggerProvider: CwpLoggerProvider = CwpLoggerProvider(),
) {
    companion object {
        val NONE = CwpRatingCorSettings()
    }
}