package com.crowdproj.rating.common.helper

import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingError

fun Throwable.asCwpRatingError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = CwpRatingError(
    code = code,
    group = group,
    field = "",
    title = code,
    description = message,
    exception = this,
)

fun CwpRatingContext.addError(vararg error: CwpRatingError) = errors.addAll(error)
