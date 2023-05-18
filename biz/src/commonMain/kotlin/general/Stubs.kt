package com.crowdproj.rating.biz.general

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingState
import com.crowdproj.rating.common.model.CwpRatingWorkMode

fun ICorAddExecDsl<CwpRatingContext>.stubs(title: String, block: ICorAddExecDsl<CwpRatingContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == CwpRatingWorkMode.STUB && state == CwpRatingState.RUNNING }
}