package com.crowdproj.rating.biz.permission

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.rating.auth.resolveFrontPermissions
import com.crowdproj.rating.auth.resolveRelationsTo
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingState

fun ICorAddExecDsl<CwpRatingContext>.frontPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление разрешений пользователей для фронтенда"

    on { state == CwpRatingState.RUNNING }

    handle {
        ratingRepoDone.permissionsClient.addAll(
            resolveFrontPermissions(
                permissionsChain,
                // Повторно вычисляем отношения, поскольку они могли измениться при выполении операции
                ratingRepoDone.resolveRelationsTo(principal)
            )
        )

        for (rating in ratingsRepoDone) {
            rating.permissionsClient.addAll(
                resolveFrontPermissions(
                    permissionsChain,
                    rating.resolveRelationsTo(principal)
                )
            )
        }
    }
}
