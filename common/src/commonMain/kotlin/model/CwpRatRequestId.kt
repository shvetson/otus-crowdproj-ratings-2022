package com.crowdproj.rating.common.model

import kotlin.jvm.JvmInline

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 12:43
 */

@JvmInline
value class CwpRatRequestId (private val id: String) {
    fun asString() = id

    companion object {
        val NONE = CwpRatRequestId("")
    }
}