package com.crowdproj.rating.repo.inmemory

import com.crowdproj.rating.common.repo.IRatingRepository
import com.crowdproj.rating.repo.tests.RepoRatingSearchTest

class RatingRepoInMemorySearchTest : RepoRatingSearchTest() {
    override val repo: IRatingRepository = RatingRepoInMemory(
        initObjects = initObjects
    )
}