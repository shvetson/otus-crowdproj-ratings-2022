package com.crowdproj.rating.repo.gremlin

import com.crowdproj.rating.common.model.CwpRating
import com.crowdproj.rating.repo.tests.RepoRatingUpdateTest

class RatingRepoGremlinUpdateTest: RepoRatingUpdateTest() {
    override val repo: RatingRepoGremlin by lazy {
        RatingRepoGremlin(
            hosts = "localhost",
            port = 8182,
            enableSsl = false,
            user = "root",
            pass = "root_root",
            initObjects = initObjects,
            initRepo = { g -> g.V().drop().iterate() },
            randomUuid = { lockNew.asString() },
        )
    }
    override val updateSucc: CwpRating by lazy { repo.initializedObjects[0] }
    override val updateConc: CwpRating by lazy { repo.initializedObjects[1] }
}