package models

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 12:31
 */

data class MkplError(
    var code: String = "",
    var group: String = "",
    var field: String = "",
    var message: String = "",
    var exception: Throwable? = null,
)