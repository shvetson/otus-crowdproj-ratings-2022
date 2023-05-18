package com.crowdproj.rating.biz.repo

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingState

fun ICorAddExecDsl<CwpRatingContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == CwpRatingState.RUNNING }
    handle {
        ratingRepoRead = ratingValidated.deepCopy()
        ratingRepoPrepare = ratingRepoRead
    }
}