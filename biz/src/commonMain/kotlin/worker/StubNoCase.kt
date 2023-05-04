package com.crowdproj.rating.biz.worker

import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.helper.fail
import com.crowdproj.rating.common.model.CwpRatingError
import com.crowdproj.rating.common.model.CwpRatingState
import com.crowdproj.rating.cor.ICorChainDsl
import com.crowdproj.rating.cor.worker

fun ICorChainDsl<CwpRatingContext>.stubNoCase(title: String) = worker {
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