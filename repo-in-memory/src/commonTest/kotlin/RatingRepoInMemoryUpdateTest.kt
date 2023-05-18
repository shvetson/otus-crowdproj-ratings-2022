package com.crowdproj.rating.repo.inmemory

import com.crowdproj.rating.common.repo.IRatingRepository
import com.crowdproj.rating.repo.tests.RepoRatingUpdateTest

class RatingRepoInMemoryUpdateTest : RepoRatingUpdateTest() {
    override val repo: IRatingRepository = RatingRepoInMemory(
        initObjects = initObjects,
//        randomUuid = { lockNew.asString() }
    )
}