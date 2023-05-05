package com.crowdproj.rating.biz.validation

import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingState
import com.crowdproj.rating.cor.ICorChainDsl
import com.crowdproj.rating.cor.worker

fun ICorChainDsl<CwpRatingContext>.finishRatingValidation(title: String) = worker {
    this.title = title
    on { state == CwpRatingState.RUNNING }
    handle {
        ratingValidated = ratingValidating
    }
}

fun ICorChainDsl<CwpRatingContext>.finishRatingFilterValidation(title: String) = worker {
    this.title = title
    on { state == CwpRatingState.RUNNING }
    handle {
        ratingFilterValidated = ratingFilterValidating
    }
}