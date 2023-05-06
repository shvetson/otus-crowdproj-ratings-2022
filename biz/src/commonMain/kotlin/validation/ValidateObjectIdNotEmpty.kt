package com.crowdproj.rating.biz.validation

import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.helper.errorValidation
import com.crowdproj.rating.common.helper.fail
import com.crowdproj.rating.cor.ICorChainDsl
import com.crowdproj.rating.cor.worker

fun ICorChainDsl<CwpRatingContext>.validateObjectIdNotEmpty(title: String) = worker {
    this.title = title
    on { ratingValidating.objectId.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "objectId",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}