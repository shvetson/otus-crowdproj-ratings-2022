package com.crowdproj.rating.biz.worker

import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.*
import com.crowdproj.rating.common.stub.CwpRatingStubs
import com.crowdproj.rating.cor.ICorChainDsl
import com.crowdproj.rating.cor.worker
import com.crowdproj.rating.stubs.CwpRatingStub

fun ICorChainDsl<CwpRatingContext>.stubUpdateSuccess(title: String) = worker {
    this.title = title
    on { stubCase == CwpRatingStubs.SUCCESS && state == CwpRatingState.RUNNING }
    handle {
        state = CwpRatingState.FINISHING
        val stub = CwpRatingStub.prepareResult {
            ratingRequest.id.takeIf { it != CwpRatingId.NONE }?.also { this.id = it }
            ratingRequest.scoreTypeId.takeIf { it != CwpRatingScoreTypeId.NONE }?.also { this.scoreTypeId = it }
            ratingRequest.objectId.takeIf { it != CwpRatingObjectId.NONE }?.also { this.objectId = it }
            ratingRequest.objectTypeId.takeIf { it != CwpRatingObjectTypeId.NONE }?.also { this.objectTypeId = it }
        }
        ratingResponse = stub
    }
}
