package com.crowdproj.rating.repo.inmemory

import com.crowdproj.rating.common.repo.IRatingRepository
import com.crowdproj.rating.repo.tests.RepoRatingDeleteTest

class RatingRepoInMemoryDeleteTest : RepoRatingDeleteTest() {
    override val repo: IRatingRepository = RatingRepoInMemory(
        initObjects = initObjects
    )
}