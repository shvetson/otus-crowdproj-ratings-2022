package com.crowdproj.rating.biz.repo

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingState
import com.crowdproj.rating.common.repo.DbRatingRequest

fun ICorAddExecDsl<CwpRatingContext>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == CwpRatingState.RUNNING }
    handle {
        val request = DbRatingRequest(ratingRepoPrepare)
        val result = ratingRepo.updateRating(request)
        val resultRating = result.data
        if (result.isSuccess && resultRating != null) {
            ratingRepoDone = resultRating
        } else {
            state = CwpRatingState.FINISHING
            errors.addAll(result.errors)
            ratingRepoDone
        }
    }
}