package com.crowdproj.rating.testing.domain

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  17.02.2023 10:00
 */

data class RatingScore(
    var ratingObj: RatingObj?
) {
    constructor() : this(
        ratingObj = null
    )
}