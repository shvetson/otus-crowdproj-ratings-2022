package com.crowdproj.rating.common.repo

import com.crowdproj.rating.common.model.CwpRating
import com.crowdproj.rating.common.model.CwpRatingError

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.05.2023 23:02
 */

data class DbRatingsResponse(
    override val data: List<CwpRating>?,
    override val isSuccess: Boolean,
    override val errors: List<CwpRatingError> = emptyList(),
): IDbResponse<List<CwpRating>> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbRatingsResponse(null, true)
        fun success(result: List<CwpRating>) = DbRatingsResponse(result, true)
        fun error(errors: List<CwpRatingError>) = DbRatingsResponse(null, false, errors)
        fun error(error: CwpRatingError) = DbRatingsResponse(null, false, listOf(error))
    }
}