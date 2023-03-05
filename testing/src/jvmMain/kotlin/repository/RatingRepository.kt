package repository

import domain.RatingScore

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  17.02.2023 11:24
 */

interface RatingRepository {
    fun getAll(): List<RatingScore>
    fun save(ratingScore: RatingScore): Boolean
    fun delete(ratingScore: RatingScore): Boolean
    fun update(currentRatingScore: RatingScore, updatedRatingScore: RatingScore): Boolean
    fun findByName(nameObjectForRating: String): RatingScore
}