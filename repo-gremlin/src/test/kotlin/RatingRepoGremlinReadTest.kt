package com.crowdproj.rating.repo.gremlin

import com.crowdproj.rating.common.model.CwpRating
import com.crowdproj.rating.repo.tests.RepoRatingReadTest

class RatingRepoGremlinReadTest : RepoRatingReadTest() {
    override val repo: RatingRepoGremlin by lazy {
        RatingRepoGremlin(
            hosts = "localhost",
            port = 8182,
            user = "root",
            pass = "root_root",
            enableSsl = false,
            initObjects = initObjects,
            initRepo = { g -> g.V().drop().iterate() },
        )
    }
    override val readSucc: CwpRating by lazy { repo.initializedObjects[0] }
}