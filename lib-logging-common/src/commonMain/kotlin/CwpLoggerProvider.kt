package com.crowdproj.rating.logging.common

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class CwpLoggerProvider(
    private val provider: (String) -> ICwpLogWrapper = { ICwpLogWrapper.DEFAULT }
) {
    fun logger(loggerId: String) = provider(loggerId)
    fun logger(clazz: KClass<*>) = provider(clazz.qualifiedName ?: clazz.simpleName ?: "(unknown)")

    fun logger(function: KFunction<*>) = provider(function.name)
}