package com.crowdproj.rating.biz.general

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingState
import com.crowdproj.rating.common.model.CwpRatingWorkMode

fun ICorAddExecDsl<CwpRatingContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != CwpRatingWorkMode.STUB }
    handle {
        ratingResponse = ratingRepoDone
        ratingsResponse = ratingsRepoDone
        state = when (val st = state) {
            CwpRatingState.RUNNING -> CwpRatingState.FINISHING
            else -> st
        }
    }
}