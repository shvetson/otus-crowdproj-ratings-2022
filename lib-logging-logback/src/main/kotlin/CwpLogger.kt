package com.crowdproj.rating.logging.logback

import ch.qos.logback.classic.Logger
import com.crowdproj.rating.logging.common.ICwpLogWrapper
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

/**
 * Generate internal MpLogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun ratingLoggerLogback(logger: Logger): ICwpLogWrapper = CwpLogWrapperLogback(
    logger = logger,
    loggerId = logger.name,
)

fun ratingLoggerLogback(clazz: KClass<*>): ICwpLogWrapper =
    ratingLoggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)

@Suppress("unused")
fun ratingLoggerLogback(loggerId: String): ICwpLogWrapper = ratingLoggerLogback(LoggerFactory.getLogger(loggerId) as Logger)