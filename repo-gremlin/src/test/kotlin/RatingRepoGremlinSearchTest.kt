package com.crowdproj.rating.repo.gremlin

import com.crowdproj.rating.common.model.CwpRating
import com.crowdproj.rating.repo.tests.RepoRatingSearchTest

class RatingRepoGremlinSearchTest: RepoRatingSearchTest() {
    override val repo: RatingRepoGremlin by lazy {
        RatingRepoGremlin(
            hosts = "localhost",
            port = 8182,
            enableSsl = false,
            user = "root",
            pass = "root_root",
            initObjects = initObjects,
            initRepo = { g -> g.V().drop().iterate() },
        )
    }

    override val initializedObjects: List<CwpRating> by lazy {
        repo.initializedObjects
    }
}