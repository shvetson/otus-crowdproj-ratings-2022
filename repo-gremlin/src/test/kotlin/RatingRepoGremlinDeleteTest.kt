package com.crowdproj.rating.repo.gremlin

import com.crowdproj.rating.common.model.CwpRating
import com.crowdproj.rating.common.model.CwpRatingId
import com.crowdproj.rating.repo.tests.RepoRatingDeleteTest

class RatingRepoGremlinDeleteTest : RepoRatingDeleteTest() {
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
    override val deleteSucc: CwpRating by lazy { repo.initializedObjects[0] }
    override val deleteConc: CwpRating by lazy { repo.initializedObjects[1] }
    override val notFoundId: CwpRatingId = CwpRatingId("#3100:0")
}