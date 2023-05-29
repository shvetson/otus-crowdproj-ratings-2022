package com.crowdproj.rating.repo.cassandra

import com.crowdproj.rating.common.model.CwpRating
import com.crowdproj.rating.common.model.CwpRatingLock
import com.crowdproj.rating.common.repo.IRatingRepository
import com.crowdproj.rating.repo.tests.*
import org.testcontainers.containers.CassandraContainer
import java.time.Duration

class RepoRatingCassandraCreateTest : RepoRatingCreateTest() {
    override val repo: IRatingRepository = TestCompanion.repository(initObjects, "ks_create", lockNew)
}

class RepoRatingCassandraDeleteTest : RepoRatingDeleteTest() {
    override val repo: IRatingRepository = TestCompanion.repository(initObjects, "ks_delete", lockOld)
}

class RepoRatingCassandraReadTest : RepoRatingReadTest() {
    override val repo: IRatingRepository = TestCompanion.repository(initObjects, "ks_read", CwpRatingLock(""))
}

class RepoRatingCassandraSearchTest : RepoRatingSearchTest() {
    override val repo: IRatingRepository = TestCompanion.repository(initObjects, "ks_search", CwpRatingLock(""))
}

class RepoRatingCassandraUpdateTest : RepoRatingUpdateTest() {
    override val repo: IRatingRepository = TestCompanion.repository(initObjects, "ks_update", lockNew)
}

class TestCasandraContainer : CassandraContainer<TestCasandraContainer>("cassandra:3.11.2")

object TestCompanion {
    private val container by lazy {
        TestCasandraContainer().withStartupTimeout(Duration.ofSeconds(300L))
            .also { it.start() }
    }

    fun repository(initObjects: List<CwpRating>, keyspace: String, lock: CwpRatingLock): RepoRatingCassandra {
        return RepoRatingCassandra(
            keyspaceName = keyspace,
            host = container.host,
            port = container.getMappedPort(CassandraContainer.CQL_PORT),
            testing = true, randomUuid = { lock.asString() }, initObjects = initObjects
        )
    }
}