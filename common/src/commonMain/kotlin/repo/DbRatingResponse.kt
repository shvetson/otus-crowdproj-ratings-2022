package com.crowdproj.rating.common.repo

import com.crowdproj.rating.common.model.CwpRating
import com.crowdproj.rating.common.model.CwpRatingError
import com.crowdproj.rating.common.helper.errorNotFound as cwpErrorNotFound
import com.crowdproj.rating.common.helper.errorSave as cwpErrorSave
import com.crowdproj.rating.common.helper.errorEmptyId as cwpErrorEmptyId

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.05.2023 23:02
 */

data class DbRatingResponse(
    override val data: CwpRating?,
    override val isSuccess: Boolean,
    override val errors: List<CwpRatingError> = emptyList(),
): IDbResponse<CwpRating> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbRatingResponse(null, true)
        fun success(result: CwpRating, isSuccess: Boolean = true) = DbRatingResponse(result, isSuccess)
        fun error(errors: List<CwpRatingError>) = DbRatingResponse(null, false, errors)
        fun error(error: CwpRatingError) = DbRatingResponse(null, false, listOf(error))

        val errorNotFound = error(cwpErrorNotFound)
        val errorSave = error(cwpErrorSave)
        val errorEmptyId = error(cwpErrorEmptyId)
    }
}