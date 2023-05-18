package com.crowdproj.rating.repo.tests

import com.crowdproj.rating.common.model.*
import com.crowdproj.rating.common.repo.DbRatingRequest
import com.crowdproj.rating.common.repo.IRatingRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoRatingCreateTest {
    abstract val repo: IRatingRepository

//    protected open val lockNew: CwpRatingLock = CwpRatingLock("20000000-0000-0000-0000-000000000002")

    private val createObj = CwpRating(
        scoreTypeId = CwpRatingScoreTypeId("1"),
        objectTypeId = CwpRatingObjectTypeId("11"),
        objectId = CwpRatingObjectId("111"),
        ownerId = CwpRatingUserId("owner-123"),
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createRating(DbRatingRequest(createObj))
        val expected = createObj.copy(id = result.data?.id ?: CwpRatingId.NONE)

        assertEquals(true, result.isSuccess)
        assertEquals(expected.scoreTypeId, result.data?.scoreTypeId)
        assertEquals(expected.objectTypeId, result.data?.objectTypeId)
        assertEquals(expected.objectId, result.data?.objectId)
        assertNotEquals(CwpRatingId.NONE, result.data?.id)
        assertEquals(emptyList(), result.errors)
//        assertEquals(lockNew, result.data?.lock)
    }

    companion object : BaseInitRatings("create") {
        override val initObjects: List<CwpRating> = emptyList()
    }
}