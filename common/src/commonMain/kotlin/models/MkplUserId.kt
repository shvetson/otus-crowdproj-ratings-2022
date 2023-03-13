package models

import kotlin.jvm.JvmInline

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 13:13
 */

@JvmInline
value class MkplUserId(private val id: String){
    fun asString() = id

    companion object {
        val NONE = MkplUserId("")
    }
}