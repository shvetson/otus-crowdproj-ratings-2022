package com.crowdproj.rating.repo.inmemory

import com.benasher44.uuid.uuid4
import com.crowdproj.rating.common.helper.errorRepoConcurrency
import com.crowdproj.rating.common.model.*
import com.crowdproj.rating.common.repo.*
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  14.05.2023 12:16
 */

class RatingRepoInMemory(
    initObjects: List<CwpRating> = emptyList(),
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : IRatingRepository {

    private val cache = Cache.Builder<String, CwpRatingEntity>()
        .expireAfterWrite(ttl)
        .build()

    private val mutex: Mutex = Mutex()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(rating: CwpRating) {
        val entity = CwpRatingEntity(rating)
        if (entity.id == null) {
            return
        }
        cache.put(entity.id, entity)
    }

    override suspend fun createRating(rq: DbRatingRequest): DbRatingResponse {
        val key = randomUuid()
        val rating = rq.rating.copy(id = CwpRatingId(key))
        val entity = CwpRatingEntity(rating)
        cache.put(key, entity)
        return DbRatingResponse(
            data = rating,
            isSuccess = true
        )
    }

    override suspend fun readRating(rq: DbRatingIdRequest): DbRatingResponse {
        val key = rq.id.takeIf { it != CwpRatingId.NONE }?.asString() ?: return resultErrorEmptyId
        return cache.get(key)?.let {
            DbRatingResponse(
                data = it.toInternal(),
                isSuccess = true,
            )
        } ?: resultErrorNotFound
    }

    override suspend fun updateRating(rq: DbRatingRequest): DbRatingResponse {
        val key = rq.rating.id.takeIf { it != CwpRatingId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.rating.lock.takeIf { it != CwpRatingLock.NONE }?.asString() ?: return resultErrorEmptyLock
        val newRating = rq.rating.copy()
        val entity = CwpRatingEntity(newRating)
        return mutex.withLock {
            val oldRating = cache.get(key)
            when {
                oldRating == null -> resultErrorNotFound
                oldRating.lock != oldLock -> DbRatingResponse(
                    data = oldRating.toInternal(),
                    isSuccess = false,
                    errors = listOf(
                        errorRepoConcurrency(
                            CwpRatingLock(oldLock),
                            oldRating.lock?.let { CwpRatingLock(it) })
                    )
                )

                else -> {
                    cache.put(key, entity)
                    DbRatingResponse(
                        data = newRating,
                        isSuccess = true,
                    )
                }
            }
        }
    }

    override suspend fun deleteRating(rq: DbRatingIdRequest): DbRatingResponse {
        val key = rq.id.takeIf { it != CwpRatingId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.lock.takeIf { it != CwpRatingLock.NONE }?.asString() ?: return resultErrorEmptyLock
        return mutex.withLock {
            val oldRating = cache.get(key)
            when {
                oldRating == null -> resultErrorNotFound
                oldRating.lock != oldLock -> DbRatingResponse(
                    data = oldRating.toInternal(),
                    isSuccess = false,
                    errors = listOf(
                        errorRepoConcurrency(CwpRatingLock(oldLock),
                            oldRating.lock?.let { CwpRatingLock(it) })
                    )
                )

                else -> {
                    cache.invalidate(key)
                    DbRatingResponse(
                        data = oldRating.toInternal(),
                        isSuccess = true,
                    )
                }
            }
        }
    }

    override suspend fun searchRating(rq: DbRatingFilterRequest): DbRatingsResponse {
        val result = cache.asMap().asSequence()
            .filter { entry ->
                rq.scoreTypeId.takeIf { it != CwpRatingScoreTypeId.NONE }?.let {
                    it.asString() == entry.value.scoreTypeId
                } ?: true
            }
            .filter { entry ->
                rq.objectTypeId.takeIf { it != CwpRatingObjectTypeId.NONE }?.let {
                    it.asString() == entry.value.objectTypeId
                } ?: true
            }
            .filter { entry ->
                rq.ownerId.takeIf { it != CwpRatingUserId.NONE }?.let {
                    it.asString() == entry.value.ownerId
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        return DbRatingsResponse(
            data = result,
            isSuccess = true
        )
    }

    companion object {

        val resultErrorEmptyId = DbRatingResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                CwpRatingError(
                    code = "id-empty",
                    group = "validation",
                    field = "id",
                    title = "id-empty",
                    description = "Id must not be null or blank"
                )
            )
        )
        val resultErrorEmptyLock = DbRatingResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                CwpRatingError(
                    code = "lock-empty",
                    group = "validation",
                    field = "lock",
                    title = "lock-empty",
                    description = "Lock must not be null or blank"
                )
            )
        )
        val resultErrorNotFound = DbRatingResponse(
            isSuccess = false,
            data = null,
            errors = listOf(
                CwpRatingError(
                    code = "not-found",
                    field = "id",
                    title = "not-found",
                    description = "Not Found"
                )
            )
        )
    }
}