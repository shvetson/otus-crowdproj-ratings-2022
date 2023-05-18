package com.crowdproj.rating.biz.repo

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingState

fun ICorAddExecDsl<CwpRatingContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == CwpRatingState.RUNNING }
    handle {
        ratingRepoPrepare = ratingRepoRead.deepCopy().apply {
            this.scoreTypeId = ratingValidated.scoreTypeId
            objectTypeId = ratingValidated.objectTypeId
            objectId = ratingValidated.objectId
        }
    }
}