package domain

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  17.02.2023 10:00
 */

class RatingScore(
    var ratingObj: RatingObj?
) {
    constructor() : this(
        ratingObj = null
    )
    override fun toString(): String {
        return "RatingScore(${ratingObj.toString()})"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RatingScore

        if (ratingObj != other.ratingObj) return false

        return true
    }

    override fun hashCode(): Int {
        return ratingObj?.hashCode() ?: 0
    }
}