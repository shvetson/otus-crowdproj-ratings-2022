package com.crowdproj.rating.repo.gremlin

import com.crowdproj.rating.common.repo.IRatingRepository
import com.crowdproj.rating.repo.tests.RepoRatingCreateTest
import com.crowdproj.rating.repo.tests.RepoRatingSearchTest

class RatingRepoGremlinCreateTest : RepoRatingCreateTest() {
    override val repo: IRatingRepository by lazy {
        RatingRepoGremlin(
            hosts = "localhost",
            port = 8182,
            enableSsl = false,
            user = "root",
            pass = "root_root",
            initObjects = RepoRatingSearchTest.initObjects,
            initRepo = { g -> g.V().drop().iterate() },
            randomUuid = { lockNew.asString() }
        )
    }
}