package com.crowdproj.rating.biz.validation

import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.helper.errorValidation
import com.crowdproj.rating.common.helper.fail
import com.crowdproj.rating.common.model.CwpRatingId
import com.crowdproj.rating.common.model.CwpRatingScoreTypeId
import com.crowdproj.rating.cor.ICorChainDsl
import com.crowdproj.rating.cor.worker

fun ICorChainDsl<CwpRatingContext>.validateScoreTypeIdProperFormat(title: String) = worker {
    this.title = title

    val regExp = Regex("^[0-9]+$")
    on { ratingValidating.scoreTypeId != CwpRatingScoreTypeId.NONE && !ratingValidating.scoreTypeId.asString().matches(regExp) }
    handle {
        fail(
            errorValidation(
                field = "scoreTypeId",
                violationCode = "badFormat",
                description = "value must contain only numbers"
            )
        )
    }
}