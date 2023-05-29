package com.crowdproj.rating.repo.tests

import com.crowdproj.rating.common.model.CwpRating
import com.crowdproj.rating.common.model.CwpRatingScoreTypeId
import com.crowdproj.rating.common.model.CwpRatingUserId
import com.crowdproj.rating.common.repo.DbRatingFilterRequest
import com.crowdproj.rating.common.repo.IRatingRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoRatingSearchTest {
    abstract val repo: IRatingRepository

    protected open val initializedObjects: List<CwpRating> = initObjects

    @Test
    fun searchOwner() = runRepoTest {
        val result = repo.searchRating(DbRatingFilterRequest(ownerId = searchOwnerId))
        assertEquals(true, result.isSuccess)
        val expected = listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data?.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun searchScoreType() = runRepoTest {
        val result = repo.searchRating(DbRatingFilterRequest(scoreTypeId = searchScoreTypeId))
        assertEquals(true, result.isSuccess)
        val expected = listOf(initializedObjects[2], initializedObjects[4]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data?.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitRatings("search") {

        val searchOwnerId = CwpRatingUserId("owner-124")
        val searchScoreTypeId = CwpRatingScoreTypeId("scoreType-1")

        override val initObjects: List<CwpRating> = listOf(
            createInitTestModel("rating1"),
            createInitTestModel("rating2", ownerId = searchOwnerId),
            createInitTestModel("rating3", scoreTypeId = searchScoreTypeId),
            createInitTestModel("rating4", ownerId = searchOwnerId),
            createInitTestModel("rating5", scoreTypeId = searchScoreTypeId),
        )
    }
}