package com.crowdproj.rating.stubs

import com.crowdproj.rating.common.model.CwpRating
import com.crowdproj.rating.common.model.CwpRatingId
import com.crowdproj.rating.stubs.CwpRatingStubProduct.RATING_PRODUCT


object CwpRatingStub {
    fun get(): CwpRating = RATING_PRODUCT.copy()

    fun prepareResult(block: CwpRating.() -> Unit): CwpRating = get().apply(block)

    fun prepareSearchList(filter: String) = listOf(
        cwpRating("r-666-01", filter),
        cwpRating("r-666-02", filter),
        cwpRating("r-666-03", filter),
        cwpRating("r-666-04", filter),
        cwpRating("r-666-05", filter),
        cwpRating("r-666-06", filter),
    )

    private fun cwpRating(id: String, filter: String) = cwpRating(RATING_PRODUCT, id = id, filter = filter)

    private fun cwpRating(base: CwpRating, id: String, filter: String ) = base.copy(id = CwpRatingId(id))

}
