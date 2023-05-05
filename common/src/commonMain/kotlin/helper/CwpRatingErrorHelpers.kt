package com.crowdproj.rating.common.helper

import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingError
import com.crowdproj.rating.common.model.CwpRatingState

fun Throwable.asCwpRatingError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = CwpRatingError(
    code = code,
    group = group,
    field = "",
    title = "Error",
    description = message,
    exception = this,
)

fun CwpRatingContext.addError(vararg error: CwpRatingError) = errors.addAll(error)

fun CwpRatingContext.fail(error: CwpRatingError) {
    addError(error)
    state = CwpRatingState.FINISHING
}

fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: CwpRatingError.Level = CwpRatingError.Level.ERROR,
) = CwpRatingError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    title = "Error",
    description = "Validation error for field $field: $description",
    level = level,
)
