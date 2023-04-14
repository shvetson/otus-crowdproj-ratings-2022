package com.crowdproj.rating.testing.domain

data class RatingObj(
    var nameObjectForRating: String
) {
    fun getName(): String {
        return nameObjectForRating
    }
}