package com.crowdproj.rating.common.model

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.03.2023 12:31
 */

data class CwpRatError(
    var code: String = "",
    var group: String = "",
    var field: String = "",
    var title: String = "",
    var description: String = "",
    var exception: Throwable? = null,
)