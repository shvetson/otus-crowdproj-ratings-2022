package com.crowdproj.rating.common.helper

import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.exception.RepoConcurrencyException
import com.crowdproj.rating.common.model.CwpRatingError
import com.crowdproj.rating.common.model.CwpRatingLock
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
    title = "error",
    description = "Validation error for field $field: $description",
    level = level,
)

fun errorAdministration(
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    field: String = "",
    violationCode: String,
    description: String,
    exception: Exception? = null,
    level: CwpRatingError.Level = CwpRatingError.Level.ERROR,
) = CwpRatingError(
    field = field,
    code = "administration-$violationCode",
    group = "administration",
    title = "administration-$violationCode",
    description = "Microservice management error: $description",
    level = level,
    exception = exception,
)

fun errorRepoConcurrency(
    expectedLock: CwpRatingLock,
    actualLock: CwpRatingLock?,
    exception: Exception? = null,
) = CwpRatingError(
    field = "lock",
    code = "concurrency",
    group = "repo",
    title = "concurrency",
    description = "The object has been changed concurrently by another user or process",
    exception = exception ?: RepoConcurrencyException(expectedLock, actualLock),
)

val errorEmptyId = CwpRatingError(
    code = "id-empty",
    field = "id",
    group = "cruds",
    description = "id must not be null or blank"
)

val errorNotFound = CwpRatingError(
    code = "not-found",
    field = "id",
    group = "cruds",
    title = "error",
    description = "not found",
)

val errorSave = CwpRatingError(
    code = "not-save",
    field = "row",
    group = "cruds",
    title = "error",
    description = "not save a new item",
)
