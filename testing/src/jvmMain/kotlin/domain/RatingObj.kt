package domain

class RatingObj(
    var nameObjectForRating: String
) {
    fun getName(): String {
        return nameObjectForRating
    }

    override fun toString(): String {
        return "RatingObj(nameObjectForRating='$nameObjectForRating')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RatingObj

        if (nameObjectForRating != other.nameObjectForRating) return false

        return true
    }

    override fun hashCode(): Int {
        return nameObjectForRating.hashCode()
    }
}