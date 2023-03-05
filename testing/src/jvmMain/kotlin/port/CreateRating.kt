package port

import domain.RatingScore

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  17.02.2023 10:09
 */

interface CreateRating {
    fun create(block: (RatingScore)-> Unit): RatingScore
}