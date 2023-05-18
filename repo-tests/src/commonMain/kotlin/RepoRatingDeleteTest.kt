package com.crowdproj.rating.repo.tests

import com.crowdproj.rating.common.model.CwpRating
import com.crowdproj.rating.common.model.CwpRatingId
import com.crowdproj.rating.common.repo.DbRatingIdRequest
import com.crowdproj.rating.common.repo.IRatingRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoRatingDeleteTest {
    abstract val repo: IRatingRepository
    protected open val deleteSucc = initObjects[0]
    protected open val deleteConc = initObjects[1]
    protected open val notFoundId = CwpRatingId("rating-repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        val lockOld = deleteSucc.lock
        val result = repo.deleteRating(DbRatingIdRequest(deleteSucc.id))
//        val result = repo.deleteRating(DbRatingIdRequest(deleteSucc.id, lock = lockOld))

        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.errors)
        assertEquals(lockOld, result.data?.lock)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readRating(DbRatingIdRequest(notFoundId))
//        val result = repo.readRating(DbRatingIdRequest(notFoundId, lock = lockOld))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {
        val lockOld = deleteSucc.lock
        val result = repo.deleteRating(DbRatingIdRequest(deleteConc.id))
//        val result = repo.deleteRating(DbRatingIdRequest(deleteConc.id, lock = lockBad))

        assertEquals(false, result.isSuccess)
        val error = result.errors.find { it.code == "concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(lockOld, result.data?.lock)
    }

    companion object : BaseInitRatings("delete") {
        override val initObjects: List<CwpRating> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
    }
}
