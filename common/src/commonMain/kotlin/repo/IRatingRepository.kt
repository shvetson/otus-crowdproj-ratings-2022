package com.crowdproj.rating.common.repo

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.05.2023 22:57
 */

interface IRatingRepository {
    suspend fun createRating(rq: DbRatingRequest): DbRatingResponse
    suspend fun readRating(rq: DbRatingIdRequest): DbRatingResponse
    suspend fun updateRating(rq: DbRatingRequest): DbRatingResponse
    suspend fun deleteRating(rq: DbRatingIdRequest): DbRatingResponse
    suspend fun searchRating(rq: DbRatingFilterRequest): DbRatingsResponse

    companion object {
        val NONE = object: IRatingRepository {
            override suspend fun createRating(rq: DbRatingRequest): DbRatingResponse {
                TODO("Not yet implemented")
            }

            override suspend fun readRating(rq: DbRatingIdRequest): DbRatingResponse {
                TODO("Not yet implemented")
            }

            override suspend fun updateRating(rq: DbRatingRequest): DbRatingResponse {
                TODO("Not yet implemented")
            }

            override suspend fun deleteRating(rq: DbRatingIdRequest): DbRatingResponse {
                TODO("Not yet implemented")
            }

            override suspend fun searchRating(rq: DbRatingFilterRequest): DbRatingsResponse {
                TODO("Not yet implemented")
            }

        }
    }
}