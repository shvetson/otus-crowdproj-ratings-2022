package com.crowdproj.rating.biz.validation

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.helper.errorValidation
import com.crowdproj.rating.common.helper.fail

fun ICorAddExecDsl<CwpRatingContext>.validateObjectTypeIdNotEmpty(title: String) = worker {
    this.title = title
    on { ratingValidating.objectTypeId.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "objectTypeId",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}