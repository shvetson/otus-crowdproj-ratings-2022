package com.crowdproj.rating.common.model

import kotlin.jvm.JvmInline

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 13:12
 */

@JvmInline
value class CwpRatObjectId(private val id: String){
    fun asString() = id

    companion object {
        val NONE = CwpRatObjectId("")
    }
}