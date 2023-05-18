package com.crowdproj.rating.repo.tests

import com.crowdproj.rating.common.model.*
import com.crowdproj.rating.common.repo.DbRatingRequest
import com.crowdproj.rating.common.repo.IRatingRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoRatingUpdateTest {
    abstract val repo: IRatingRepository
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]
    protected val updateIdNotFound = CwpRatingId("rating-repo-update-not-found")
//    protected val lockBad = CwpRatingLock("20000000-0000-0000-0000-000000000009")
//    protected val lockNew = CwpRatingLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSucc by lazy {
        CwpRating(
            id = updateSucc.id,
            scoreTypeId = CwpRatingScoreTypeId("1 updated"),
            objectTypeId = CwpRatingObjectTypeId("11 updated"),
            objectId = CwpRatingObjectId("111 updated"),
            ownerId = CwpRatingUserId("owner-123"),
//            lock = initObjects.first().lock,
        )
    }
    private val reqUpdateNotFound = CwpRating(
        id = updateIdNotFound,
        scoreTypeId = CwpRatingScoreTypeId("1 update object not found"),
        objectTypeId = CwpRatingObjectTypeId("11 update object not found"),
        objectId = CwpRatingObjectId("111 update object not found"),
        ownerId = CwpRatingUserId("owner-123"),
//        lock = initObjects.first().lock,
    )

    private val reqUpdateConc by lazy {
        CwpRating(
            id = updateConc.id,
            scoreTypeId = CwpRatingScoreTypeId("1 update object not found"),
            objectTypeId = CwpRatingObjectTypeId("11 update object not found"),
            objectId = CwpRatingObjectId("111 update object not found"),
            ownerId = CwpRatingUserId("owner-123"),
//            lock = lockBad,
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateRating(DbRatingRequest(reqUpdateSucc))
        assertEquals(true, result.isSuccess)
        assertEquals(reqUpdateSucc.id, result.data?.id)
        assertEquals(reqUpdateSucc.scoreTypeId, result.data?.scoreTypeId)
        assertEquals(reqUpdateSucc.objectTypeId, result.data?.objectTypeId)
        assertEquals(reqUpdateSucc.objectId, result.data?.objectId)
        assertEquals(emptyList(), result.errors)
//        assertEquals(lockNew, result.data?.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateRating(DbRatingRequest(reqUpdateNotFound))
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateRating(DbRatingRequest(reqUpdateConc))
        assertEquals(false, result.isSuccess)
        val error = result.errors.find { it.code == "concurrency" }
//        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object : BaseInitRatings("update") {
        override val initObjects: List<CwpRating> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}
