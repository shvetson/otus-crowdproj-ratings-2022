package com.crowdproj.rating.biz.validation

import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingState
import com.crowdproj.rating.cor.ICorChainDsl
import com.crowdproj.rating.cor.chain

fun ICorChainDsl<CwpRatingContext>.validation(block: ICorChainDsl<CwpRatingContext>.() -> Unit) = chain {
    block()
    title = "Валидация"
    on { state == CwpRatingState.RUNNING }
}