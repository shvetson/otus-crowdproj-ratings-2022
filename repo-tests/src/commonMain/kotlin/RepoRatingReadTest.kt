package com.crowdproj.rating.repo.tests

import com.crowdproj.rating.common.model.CwpRating
import com.crowdproj.rating.common.model.CwpRatingId
import com.crowdproj.rating.common.repo.DbRatingIdRequest
import com.crowdproj.rating.common.repo.IRatingRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoRatingReadTest {
    abstract val repo: IRatingRepository
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readRating(DbRatingIdRequest(readSucc.id))
        assertEquals(true, result.isSuccess)
        assertEquals(readSucc, result.data)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readRating(DbRatingIdRequest(notFoundId))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitRatings("read") {
        override val initObjects: List<CwpRating> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = CwpRatingId("rating-repo-read-notFound")
    }
}