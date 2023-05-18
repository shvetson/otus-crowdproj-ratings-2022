package com.crowdproj.rating.repo.inmemory

import com.crowdproj.rating.repo.tests.RepoRatingCreateTest

class RatingRepoInMemoryCreateTest : RepoRatingCreateTest() {
    override val repo = RatingRepoInMemory(
        initObjects = initObjects,
//        randomUuid = { lockNew.asString() }
    )
}