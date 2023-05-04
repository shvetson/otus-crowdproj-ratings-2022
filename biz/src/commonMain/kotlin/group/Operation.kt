package com.crowdproj.rating.biz.group

import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingCommand
import com.crowdproj.rating.common.model.CwpRatingState
import com.crowdproj.rating.cor.ICorChainDsl
import com.crowdproj.rating.cor.chain


fun ICorChainDsl<CwpRatingContext>.operation(title: String, command: CwpRatingCommand, block: ICorChainDsl<CwpRatingContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { this.command == command && state == CwpRatingState.RUNNING }
}
