package com.crowdproj.rating.biz

import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.stubs.CwpRatingStub

class CwpRatingProcessor {
    suspend fun exec(ctx: CwpRatingContext) {
        ctx.ratingResponse = CwpRatingStub.get()
    }
}
