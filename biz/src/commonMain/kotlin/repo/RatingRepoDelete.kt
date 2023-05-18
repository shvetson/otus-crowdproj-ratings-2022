package com.crowdproj.rating.biz.repo

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingState
import com.crowdproj.rating.common.repo.DbRatingIdRequest

fun ICorAddExecDsl<CwpRatingContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление объявления из БД по ID"
    on { state == CwpRatingState.RUNNING }
    handle {
        val request = DbRatingIdRequest(ratingRepoPrepare)
        val result = ratingRepo.deleteRating(request)
        if (!result.isSuccess) {
            state = CwpRatingState.FINISHING
            errors.addAll(result.errors)
        }
        ratingRepoDone = ratingRepoRead
    }
}