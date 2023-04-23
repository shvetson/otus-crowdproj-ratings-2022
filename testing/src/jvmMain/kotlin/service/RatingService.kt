package com.crowdproj.rating.testing.service

import com.crowdproj.rating.testing.domain.RatingObj
import com.crowdproj.rating.testing.domain.RatingScore
import com.crowdproj.rating.testing.port.CreateRating
import com.crowdproj.rating.testing.port.CrudsRating
import com.crowdproj.rating.testing.repository.RatingRepository

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  17.02.2023 10:12
 */

class RatingService(
    private val ratingRepository: RatingRepository
): CreateRating, CrudsRating {

    override fun create(block: (RatingScore) -> Unit): RatingScore {
        val rating = RatingScore()
        block(rating)
        return rating
    }

    override fun getAll(): List<RatingScore> {
        return ratingRepository.getAll()
    }

    override fun save(ratingObj: RatingObj): Boolean {
        val ratingScore = RatingScore().apply {
            this.ratingObj = ratingObj
        }
        return ratingRepository.save(ratingScore)
    }

    override fun delete(ratingObj: RatingObj): Boolean {
        val ratingScore = find(ratingObj)
        return ratingRepository.delete(ratingScore)
    }

    override fun update(
        currentNameObjectForRating: String,
        newNameObjectForRating: String
    ): Boolean {
        val currentRatingScore: RatingScore = ratingRepository.findByName(currentNameObjectForRating)
        val currentObjectForRating = currentRatingScore.ratingObj

        val updatedObjectForRating = currentObjectForRating.apply {
            this?.nameObjectForRating = newNameObjectForRating
        }

        val updatedRatingScore = RatingScore().apply {
            this.ratingObj = updatedObjectForRating
        }

        return ratingRepository.update(currentRatingScore, updatedRatingScore)
    }

    override fun findByName(nameObjectForRating: String): RatingScore {
        return ratingRepository.findByName(nameObjectForRating)
    }

    private fun find(ratingObj: RatingObj): RatingScore {
        val name = ratingObj.getName()
        return findByName(name)
    }
}