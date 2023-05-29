package com.crowdproj.rating.common.repo

import com.crowdproj.rating.common.model.CwpRating
import com.crowdproj.rating.common.model.CwpRatingId
import com.crowdproj.rating.common.model.CwpRatingLock

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.05.2023 23:09
 */

data class DbRatingIdRequest(
    val id: CwpRatingId,
    val lock: CwpRatingLock = CwpRatingLock.NONE,
) {
    constructor(rating: CwpRating) : this(rating.id, rating.lock)
}