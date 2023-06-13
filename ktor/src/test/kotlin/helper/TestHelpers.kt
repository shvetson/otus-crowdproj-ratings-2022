package com.crowdproj.rating.ktor.helper

import com.crowdproj.rating.common.CwpRatingCorSettings
import com.crowdproj.rating.common.repo.IRatingRepository
import com.crowdproj.rating.ktor.CwpRatingAppSettings
import com.crowdproj.rating.ktor.base.KtorAuthConfig
import com.crowdproj.rating.repo.inmemory.RatingRepoInMemory
import com.crowdproj.rating.repo.stubs.RatingRepoStub

fun testSettings(repo: IRatingRepository? = null) = CwpRatingAppSettings(
    corSettings = CwpRatingCorSettings(
        repoStub = RatingRepoStub(),
        repoTest = repo ?: RatingRepoInMemory(),
        repoProd = repo ?: RatingRepoInMemory(),
    ),
    auth = KtorAuthConfig.TEST
)