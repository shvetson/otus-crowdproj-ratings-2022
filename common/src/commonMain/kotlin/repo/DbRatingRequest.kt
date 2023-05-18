package com.crowdproj.rating.common.repo

import com.crowdproj.rating.common.model.CwpRating

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.05.2023 23:14
 */

data class DbRatingRequest(
    val rating: CwpRating
)
