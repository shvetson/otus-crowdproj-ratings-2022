package com.crowdproj.rating.biz.general

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingCommand
import com.crowdproj.rating.common.model.CwpRatingState

fun ICorAddExecDsl<CwpRatingContext>.operation(title: String, command: CwpRatingCommand, block: ICorAddExecDsl<CwpRatingContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { this.command == command && state == CwpRatingState.RUNNING }
}