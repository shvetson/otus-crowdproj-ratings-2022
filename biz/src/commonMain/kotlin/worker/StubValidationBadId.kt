package com.crowdproj.rating.biz.worker

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingError
import com.crowdproj.rating.common.model.CwpRatingState
import com.crowdproj.rating.common.stub.CwpRatingStubs

fun ICorAddExecDsl<CwpRatingContext>.stubValidationBadId(title: String) = worker {
    this.title = title
    on { stubCase == CwpRatingStubs.BAD_ID && state == CwpRatingState.RUNNING }
    handle {
        state = CwpRatingState.FINISHING
        this.errors.add(
            CwpRatingError(
                group = "validation",
                code = "validation-id",
                field = "id",
                title = "validation-id",
                description = "Wrong id field"
            )
        )
    }
}