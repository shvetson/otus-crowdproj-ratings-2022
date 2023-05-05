package com.crowdproj.rating.spring.config

import com.crowdproj.rating.common.CwpRatingCorSettings
import com.crowdproj.rating.logging.common.CwpLoggerProvider
import com.crowdproj.rating.logging.logback.ratingLoggerLogback
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CorConfig {
    @Bean
    fun loggerProvider(): CwpLoggerProvider = CwpLoggerProvider { ratingLoggerLogback(it) }

    @Bean
    fun corSettings(): CwpRatingCorSettings = CwpRatingCorSettings(
        loggerProvider = loggerProvider()
    )
}