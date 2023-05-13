package com.crowdproj.rating.biz.validation

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.helper.errorValidation
import com.crowdproj.rating.common.helper.fail
import com.crowdproj.rating.common.model.CwpRatingId

fun ICorAddExecDsl<CwpRatingContext>.validateIdProperFormat(title: String) = worker {
    this.title = title

    val regExp = Regex("^[0-9]+$")
    on { ratingValidating.id != CwpRatingId.NONE && !ratingValidating.id.asString().matches(regExp) }
    handle {
        fail(
            errorValidation(
                field = "id",
                violationCode = "badFormat",
                description = "value must contain only numbers"
            )
        )
    }
}