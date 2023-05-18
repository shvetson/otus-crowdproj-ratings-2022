package com.crowdproj.rating.common.model

import kotlin.jvm.JvmInline

@JvmInline
value class CwpRatingLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = CwpRatingLock("")
    }
}
