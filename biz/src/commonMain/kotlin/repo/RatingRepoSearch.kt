package com.crowdproj.rating.biz.repo

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingState
import com.crowdproj.rating.common.repo.DbRatingFilterRequest

fun ICorAddExecDsl<CwpRatingContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск объявлений в БД по фильтру"
    on { state == CwpRatingState.RUNNING }
    handle {
        val request = DbRatingFilterRequest(
            scoreTypeId = ratingFilterValidated.scoreTypeId,
            objectTypeId = ratingFilterValidated.objectTypeId,
            ownerId = ratingFilterValidated.ownerId,
//            scoreTypeId = ratingFilterValidated.scoreTypeId,
//            objectTypeId = ratingFilterValidated.objectTypeId,
//            ownerId = ratingFilterValidated.ownerId,
        )
        val result = ratingRepo.searchRating(request)
        val resultRatings = result.data
        if (result.isSuccess && resultRatings != null) {
            ratingsRepoDone = resultRatings.toMutableList()
        } else {
            state = CwpRatingState.FINISHING
            errors.addAll(result.errors)
        }
    }
}