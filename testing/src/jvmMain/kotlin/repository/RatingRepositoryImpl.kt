package com.crowdproj.rating.testing.repository

import com.crowdproj.rating.testing.domain.RatingScore

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  17.02.2023 11:28
 */

class RatingRepositoryImpl: RatingRepository {

    private val data: MutableList<RatingScore> = arrayListOf()

    override fun getAll(): List<RatingScore> {
        return data.toList()
    }

    override fun save(ratingScore: RatingScore): Boolean {
        return data.add(ratingScore)
    }

    override fun delete(ratingScore: RatingScore): Boolean {
        return data.remove(ratingScore)
    }

    override fun update(currentRatingScore: RatingScore, updatedRatingScore: RatingScore): Boolean {
        val index = data.indexOf(currentRatingScore)
        if (index == -1) return false
        data.add(index, updatedRatingScore)
        return true
    }

    override fun findByName(nameObjectForRating: String): RatingScore {
        return data.stream().filter { it.ratingObj?.getName() == nameObjectForRating }.findFirst().get()
    }
}