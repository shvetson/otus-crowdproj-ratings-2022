package com.crowdproj.rating.common.repo

import com.crowdproj.rating.common.model.CwpRatingError

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.05.2023 22:59
 */

interface IDbResponse<T> {
    val data: T?
    val isSuccess: Boolean
    val errors: List<CwpRatingError>
}