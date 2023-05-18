package com.crowdproj.rating.repo.stubs

import com.crowdproj.rating.common.repo.*
import com.crowdproj.rating.stubs.CwpRatingStub

class RatingRepoStub() : IRatingRepository {
    override suspend fun createRating(rq: DbRatingRequest): DbRatingResponse {
        return DbRatingResponse(
            data = CwpRatingStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun readRating(rq: DbRatingIdRequest): DbRatingResponse {
        return DbRatingResponse(
            data = CwpRatingStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun updateRating(rq: DbRatingRequest): DbRatingResponse {
        return DbRatingResponse(
            data = CwpRatingStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun deleteRating(rq: DbRatingIdRequest): DbRatingResponse {
        return DbRatingResponse(
            data = CwpRatingStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun searchRating(rq: DbRatingFilterRequest): DbRatingsResponse {
        return DbRatingsResponse(
            data = CwpRatingStub.prepareSearchList(filter = ""),
            isSuccess = true,
        )
    }
}