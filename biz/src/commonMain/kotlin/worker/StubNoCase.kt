package com.crowdproj.rating.biz.worker

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.helper.fail
import com.crowdproj.rating.common.model.CwpRatingError
import com.crowdproj.rating.common.model.CwpRatingState

fun ICorAddExecDsl<CwpRatingContext>.stubNoCase(title: String) = worker {
    this.title = title
    on { state == CwpRatingState.RUNNING }
    handle {
        fail(
            CwpRatingError(
                code = "validation",
                field = "stub",
                group = "validation",
                title = "validation",
                description = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}