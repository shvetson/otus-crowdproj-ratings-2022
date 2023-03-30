package com.crowdproj.rating.stubs

import com.crowdproj.rating.common.model.CwpRating
import com.crowdproj.rating.common.model.CwpRatingId
import com.crowdproj.rating.common.model.CwpRatingScoreTypeId
import com.crowdproj.rating.stubs.CwpRatingStubProduct.RATING_PRODUCT


object CwpRatingStub {
    fun get(): CwpRating = RATING_PRODUCT.copy()

    fun prepareResult(block: CwpRating.() -> Unit): CwpRating = get().apply(block)

    fun prepareSearchList(filter: String) = listOf(
        cwpRating("pr-111-01", filter),
        cwpRating("pr-111-02", filter),
        cwpRating("pr-111-03", filter),
        cwpRating("pr-111-04", filter),
        cwpRating("pr-111-05", filter),
        cwpRating("pr-111-06", filter),
    )

    private fun cwpRating(id: String, filter: String) = ratingCopy(RATING_PRODUCT, id = id, scoreTypeId = filter)

    private fun ratingCopy(base: CwpRating, id: String, scoreTypeId: String) = base.copy(
        id = CwpRatingId(id),
        scoreTypeId = CwpRatingScoreTypeId(scoreTypeId),
    )

}
