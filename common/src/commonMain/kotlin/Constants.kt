package com.crowdproj.rating.common

import kotlinx.datetime.Instant

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 12:48
 */

private val INSTANT_NONE = Instant.fromEpochMilliseconds(Long.MIN_VALUE)

val Instant.Companion.NONE
    get() = INSTANT_NONE