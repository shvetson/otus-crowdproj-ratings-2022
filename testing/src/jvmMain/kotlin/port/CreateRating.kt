package com.crowdproj.rating.testing.port

import com.crowdproj.rating.testing.domain.RatingScore

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  17.02.2023 10:09
 */

interface CreateRating {
    fun create(block: (RatingScore)-> Unit): RatingScore
}