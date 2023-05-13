package com.crowdproj.rating.biz.validation

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingState

fun ICorAddExecDsl<CwpRatingContext>.validation(block: ICorAddExecDsl<CwpRatingContext>.() -> Unit) = chain {
    block()
    title = "Валидация"
    on { state == CwpRatingState.RUNNING }
}