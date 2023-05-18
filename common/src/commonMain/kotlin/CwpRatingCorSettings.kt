package com.crowdproj.rating.common

import com.crowdproj.rating.logging.common.CwpLoggerProvider
import com.crowdproj.rating.common.repo.IRatingRepository

data class CwpRatingCorSettings(
    val loggerProvider: CwpLoggerProvider = CwpLoggerProvider(),
    val repoStub: IRatingRepository = IRatingRepository.NONE,
    val repoTest: IRatingRepository = IRatingRepository.NONE,
    val repoProd: IRatingRepository = IRatingRepository.NONE,
) {
    companion object {
        val NONE = CwpRatingCorSettings()
    }
}