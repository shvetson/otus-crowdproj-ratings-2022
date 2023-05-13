package com.crowdproj.rating.biz.validation

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.helper.errorValidation
import com.crowdproj.rating.common.helper.fail
import com.crowdproj.rating.common.model.CwpRatingObjectTypeId

fun ICorAddExecDsl<CwpRatingContext>.validateObjectTypeIdProperFormat(title: String) = worker {
    this.title = title

    val regExp = Regex("^[0-9]+$")
    on { ratingValidating.objectTypeId != CwpRatingObjectTypeId.NONE && !ratingValidating.objectTypeId.asString().matches(regExp) }
    handle {
        fail(
            errorValidation(
                field = "objectTypeId",
                violationCode = "badFormat",
                description = "value must contain only numbers"
            )
        )
    }
}