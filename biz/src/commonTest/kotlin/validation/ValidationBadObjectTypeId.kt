package com.crowdproj.rating.biz.validation

import com.crowdproj.rating.biz.CwpRatingProcessor
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
fun validationObjectTypeIdCorrect(command: CwpRatingCommand, processor: CwpRatingProcessor) = runTest {
    val ctx = CwpRatingContext(
        command = command,
        state = CwpRatingState.NONE,
        workMode = CwpRatingWorkMode.TEST,
        ratingRequest = CwpRating(
            id = CwpRatingId("1"),
            scoreTypeId = CwpRatingScoreTypeId("11"),
            objectId = CwpRatingObjectId("111"),
            objectTypeId = CwpRatingObjectTypeId("1111")
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(CwpRatingState.NONE, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationObjectTypeIdTrim(command: CwpRatingCommand, processor: CwpRatingProcessor) = runTest {
    val ctx = CwpRatingContext(
        command = command,
        state = CwpRatingState.NONE,
        workMode = CwpRatingWorkMode.TEST,
        ratingRequest = CwpRating(
            id = CwpRatingId("1"),
            scoreTypeId = CwpRatingScoreTypeId("11"),
            objectId = CwpRatingObjectId("111"),
            objectTypeId = CwpRatingObjectTypeId(" \n\t 1111 \n\t ")
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(CwpRatingState.NONE, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationObjectTypeIdEmpty(command: CwpRatingCommand, processor: CwpRatingProcessor) = runTest {
    val ctx = CwpRatingContext(
        command = command,
        state = CwpRatingState.NONE,
        workMode = CwpRatingWorkMode.TEST,
        ratingRequest = CwpRating(
            id = CwpRatingId("1"),
            scoreTypeId = CwpRatingScoreTypeId("11"),
            objectId = CwpRatingObjectId("111"),
            objectTypeId = CwpRatingObjectTypeId(" ")
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(CwpRatingState.FINISHING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("objectTypeId", error?.field)
    assertContains(error?.description ?: "", "objectTypeId")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationObjectTypeIdFormat(command: CwpRatingCommand, processor: CwpRatingProcessor) = runTest {
    val ctx = CwpRatingContext(
        command = command,
        state = CwpRatingState.NONE,
        workMode = CwpRatingWorkMode.TEST,
        ratingRequest = CwpRating(
            id = CwpRatingId("1"),
            scoreTypeId = CwpRatingScoreTypeId("11"),
            objectId = CwpRatingObjectId("111"),
            objectTypeId = CwpRatingObjectTypeId("!@#\$%^&*(),.{}")
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(CwpRatingState.FINISHING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("objectTypeId", error?.field)
    assertContains(error?.description ?: "", "objectTypeId")
}