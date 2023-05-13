package com.crowdproj.rating.biz.worker

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingState
import com.crowdproj.rating.common.stub.CwpRatingStubs
import com.crowdproj.rating.stubs.CwpRatingStub

fun ICorAddExecDsl<CwpRatingContext>.stubSearchSuccess(title: String) = worker {
    this.title = title
    on { stubCase == CwpRatingStubs.SUCCESS && state == CwpRatingState.RUNNING }
    handle {
        state = CwpRatingState.FINISHING
        ratingsResponse.addAll(CwpRatingStub.prepareSearchList(ratingFilterRequest.searchString))
    }
}