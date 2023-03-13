package models

import kotlin.jvm.JvmInline

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 12:43
 */

@JvmInline
value class MkplRequestId (private val id: String) {
    fun asString() = id

    companion object {
        val NONE = MkplRequestId("")
    }
}