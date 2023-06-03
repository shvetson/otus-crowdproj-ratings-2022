package com.crowdproj.rating.biz.permission

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.rating.auth.checkPermitted
import com.crowdproj.rating.auth.resolveRelationsTo
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.helper.fail
import com.crowdproj.rating.common.model.CwpRatingError
import com.crowdproj.rating.common.model.CwpRatingState

fun ICorAddExecDsl<CwpRatingContext>.accessValidation(title: String) = chain {
    this.title = title
    description = "Вычисление прав доступа по группе принципала и таблице прав доступа"
    on { state == CwpRatingState.RUNNING }
    worker("Вычисление отношения рейтинга к принципалу") {
        ratingRepoRead.principalRelations = ratingRepoRead.resolveRelationsTo(principal)
    }
    worker("Определение доступа к рейтингу") {
        permitted = checkPermitted(command, ratingRepoRead.principalRelations, permissionsChain)
    }
    worker {
        this.title = "Валидация прав доступа"
        description = "Проверка наличия прав для выполнения операции"
        on { !permitted }
        handle {
            fail(CwpRatingError(title = "User is not allowed to perform this operation"))
        }
    }
}