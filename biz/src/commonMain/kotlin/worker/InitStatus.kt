package com.crowdproj.rating.biz.worker

import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingState
import com.crowdproj.rating.cor.ICorChainDsl
import com.crowdproj.rating.cor.worker


fun ICorChainDsl<CwpRatingContext>.initStatus(title: String) = worker() {
    this.title = title
    on { state == CwpRatingState.NONE }
    handle { state = CwpRatingState.RUNNING }
}
