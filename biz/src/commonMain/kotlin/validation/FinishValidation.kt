package com.crowdproj.rating.biz.validation

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingState

fun ICorAddExecDsl<CwpRatingContext>.finishRatingValidation(title: String) = worker {
    this.title = title
    on { state == CwpRatingState.RUNNING }
    handle {
        ratingValidated = ratingValidating
    }
}

fun ICorAddExecDsl<CwpRatingContext>.finishRatingFilterValidation(title: String) = worker {
    this.title = title
    on { state == CwpRatingState.RUNNING }
    handle {
        ratingFilterValidated = ratingFilterValidating
    }
}