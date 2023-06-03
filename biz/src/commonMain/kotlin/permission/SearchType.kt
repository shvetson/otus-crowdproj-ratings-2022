package com.crowdproj.rating.biz.permission

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingSearchPermissions
import com.crowdproj.rating.common.model.CwpRatingState
import com.crowdproj.rating.common.permission.CwpRatingUserPermissions

fun ICorAddExecDsl<CwpRatingContext>.searchTypes(title: String) = chain {
    this.title = title
    description = "Добавление ограничений в поисковый запрос согласно правам доступа и др. политикам"
    on { state == CwpRatingState.RUNNING }
    worker("Определение типа поиска") {
        ratingFilterValidated.searchPermissions = setOfNotNull(
            CwpRatingSearchPermissions.OWN.takeIf { permissionsChain.contains(CwpRatingUserPermissions.SEARCH_OWN) },
            CwpRatingSearchPermissions.PUBLIC.takeIf { permissionsChain.contains(CwpRatingUserPermissions.SEARCH_PUBLIC) },
            CwpRatingSearchPermissions.REGISTERED.takeIf { permissionsChain.contains(CwpRatingUserPermissions.SEARCH_REGISTERED) },
        ).toMutableSet()
    }
}