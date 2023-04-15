package com.crowdproj.rating.testing.port

import com.crowdproj.rating.testing.domain.RatingObj
import com.crowdproj.rating.testing.domain.RatingScore

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  17.02.2023 11:17
 */

interface CrudsRating {
    fun getAll(): List<RatingScore>
    fun save(ratingObj: RatingObj): Boolean
    fun delete(ratingObj: RatingObj): Boolean
    fun update(currentNameObjectForRating: String, newNameObjectForRating: String): Boolean
    fun findByName(nameObjectForRating: String): RatingScore
}