package com.crowdproj.rating.biz.general

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.helper.errorAdministration
import com.crowdproj.rating.common.helper.fail
import com.crowdproj.rating.common.model.CwpRatingWorkMode
import com.crowdproj.rating.common.repo.IRatingRepository

fun ICorAddExecDsl<CwpRatingContext>.initRepo(title: String) = worker() {
    this.title = title
    description = "Определение репозитория на основе режима работы"
    handle {
        ratingRepo = when {
            workMode == CwpRatingWorkMode.TEST -> settings.repoTest
            workMode == CwpRatingWorkMode.STUB -> settings.repoStub
            else -> settings.repoProd
        }

        if (workMode != CwpRatingWorkMode.STUB && ratingRepo == IRatingRepository.Companion.NONE) fail(
            errorAdministration(
                field = "repo",
                violationCode = "dbNotConfigured",
                description = "Репозиторий не настроен для $workMode"
            )
        )
    }
}