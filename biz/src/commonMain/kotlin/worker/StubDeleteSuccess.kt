package com.crowdproj.rating.biz.worker

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingScoreTypeId
import com.crowdproj.rating.common.model.CwpRatingState
import com.crowdproj.rating.common.stub.CwpRatingStubs
import com.crowdproj.rating.stubs.CwpRatingStub

fun ICorAddExecDsl<CwpRatingContext>.stubDeleteSuccess(title: String) = worker {
    this.title = title
    on { stubCase == CwpRatingStubs.SUCCESS && state == CwpRatingState.RUNNING }
    handle {
        state = CwpRatingState.FINISHING
        val stub = CwpRatingStub.prepareResult {
            ratingRequest.scoreTypeId.takeIf { it != CwpRatingScoreTypeId.NONE }?.also { this.scoreTypeId = it }
        }
        ratingResponse = stub
    }
}