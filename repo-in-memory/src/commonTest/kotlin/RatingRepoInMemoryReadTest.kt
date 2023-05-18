package com.crowdproj.rating.repo.inmemory

import com.crowdproj.rating.common.repo.IRatingRepository
import com.crowdproj.rating.repo.tests.RepoRatingReadTest

class RatingRepoInMemoryReadTest : RepoRatingReadTest() {
    override val repo: IRatingRepository = RatingRepoInMemory(
        initObjects = initObjects
    )
}