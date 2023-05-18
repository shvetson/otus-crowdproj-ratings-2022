package com.crowdproj.rating.biz.repo

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingState
import com.crowdproj.rating.common.repo.DbRatingRequest

fun ICorAddExecDsl<CwpRatingContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Добавление объявления в БД"
    on { state == CwpRatingState.RUNNING }
    handle {
        val request = DbRatingRequest(ratingRepoPrepare)
        val result = ratingRepo.createRating(request)
        val resultAd = result.data
        if (result.isSuccess && resultAd != null) {
            ratingRepoDone = resultAd
        } else {
            state = CwpRatingState.FINISHING
            errors.addAll(result.errors)
        }
    }
}
