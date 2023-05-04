package com.crowdproj.rating.biz.worker

import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingError
import com.crowdproj.rating.common.model.CwpRatingState
import com.crowdproj.rating.common.stub.CwpRatingStubs
import com.crowdproj.rating.cor.ICorChainDsl
import com.crowdproj.rating.cor.worker

fun ICorChainDsl<CwpRatingContext>.stubValidationBadObjectTypeId(title: String) = worker {
    this.title = title
    on { stubCase == CwpRatingStubs.BAD_ID && state == CwpRatingState.RUNNING }
    handle {
        state = CwpRatingState.FINISHING
        this.errors.add(
            CwpRatingError(
                group = "validation",
                code = "validation-id",
                field = "ObjectTypeId",
                title = "validation-id",
                description = "Wrong id field"
            )
        )
    }
}