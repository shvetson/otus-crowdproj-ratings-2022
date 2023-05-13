package com.crowdproj.rating.biz.validation

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.helper.errorValidation
import com.crowdproj.rating.common.helper.fail
import com.crowdproj.rating.common.model.CwpRatingObjectId

fun ICorAddExecDsl<CwpRatingContext>.validateObjectIdProperFormat(title: String) = worker {
    this.title = title

    val regExp = Regex("^[0-9]+$")
    on { ratingValidating.objectId != CwpRatingObjectId.NONE && !ratingValidating.objectId.asString().matches(regExp) }
    handle {
        fail(
            errorValidation(
                field = "objectId",
                violationCode = "badFormat",
                description = "value must contain only numbers"
            )
        )
    }
}