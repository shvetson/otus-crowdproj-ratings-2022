package com.crowdproj.rating.biz.validation

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.helper.errorValidation
import com.crowdproj.rating.common.helper.fail

fun ICorAddExecDsl<CwpRatingContext>.validateScoreTypeIdNotEmpty(title: String) = worker {
    this.title = title
    on { ratingValidating.scoreTypeId.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "scoreTypeId",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}