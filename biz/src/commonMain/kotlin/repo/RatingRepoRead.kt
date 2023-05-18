package com.crowdproj.rating.biz.repo

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingState
import com.crowdproj.rating.common.repo.DbRatingIdRequest

fun ICorAddExecDsl<CwpRatingContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение объявления из БД"
    on { state == CwpRatingState.RUNNING }
    handle {
        val request = DbRatingIdRequest(ratingValidated)
        val result = ratingRepo.readRating(request)
        val resultRating = result.data
        if (result.isSuccess && resultRating != null) {
            ratingRepoRead = resultRating
        } else {
            state = CwpRatingState.FINISHING
            errors.addAll(result.errors)
        }
    }
}
