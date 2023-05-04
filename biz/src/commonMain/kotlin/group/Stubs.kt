package com.crowdproj.rating.biz.group

import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingState
import com.crowdproj.rating.common.model.CwpRatingWorkMode
import com.crowdproj.rating.cor.ICorChainDsl
import com.crowdproj.rating.cor.chain

fun ICorChainDsl<CwpRatingContext>.stubs(title: String, block: ICorChainDsl<CwpRatingContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == CwpRatingWorkMode.STUB && state == CwpRatingState.RUNNING }
}
