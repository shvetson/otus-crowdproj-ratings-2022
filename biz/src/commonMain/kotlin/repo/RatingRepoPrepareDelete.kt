package com.crowdproj.rating.biz.repo

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingState

fun ICorAddExecDsl<CwpRatingContext>.repoPrepareDelete(title: String) = worker {
    this.title = title
    description = """
        Готовим данные к удалению из БД
    """.trimIndent()
    on { state == CwpRatingState.RUNNING }
    handle {
        ratingRepoPrepare = ratingValidated.deepCopy()
    }
}