package com.crowdproj.rating.biz.general

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingState

fun ICorAddExecDsl<CwpRatingContext>.initStatus(title: String) = worker() {
    this.title = title
    on { state == CwpRatingState.NONE }
    handle { state = CwpRatingState.RUNNING }
}