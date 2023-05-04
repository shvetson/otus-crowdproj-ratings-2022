package com.crowdproj.rating.biz.worker

import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingError
import com.crowdproj.rating.common.model.CwpRatingState
import com.crowdproj.rating.common.stub.CwpRatingStubs
import com.crowdproj.rating.cor.ICorChainDsl
import com.crowdproj.rating.cor.worker


fun ICorChainDsl<CwpRatingContext>.stubDbError(title: String) = worker {
    this.title = title
    on { stubCase == CwpRatingStubs.NOT_FOUND && state == CwpRatingState.RUNNING }
    handle {
        state = CwpRatingState.FINISHING
        this.errors.add(
            CwpRatingError(
                group = "internal",
                code = "internal-db",
                title = "internal-db",
                description = "Internal error"
            )
        )
    }
}
