package com.crowdproj.rating.biz.validation

import com.crowdproj.rating.biz.CwpRatingProcessor
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingCommand
import com.crowdproj.rating.common.model.CwpRatingFilter
import com.crowdproj.rating.common.model.CwpRatingState
import com.crowdproj.rating.common.model.CwpRatingWorkMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationSearchTest {

    private val command = CwpRatingCommand.SEARCH
    private val processor by lazy { CwpRatingProcessor() }

    @Test
    fun correctEmpty() = runTest {
        val ctx = CwpRatingContext(
            command = command,
            state = CwpRatingState.NONE,
            workMode = CwpRatingWorkMode.TEST,
            ratingFilterRequest = CwpRatingFilter()
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(CwpRatingState.FINISHING, ctx.state)
    }
}