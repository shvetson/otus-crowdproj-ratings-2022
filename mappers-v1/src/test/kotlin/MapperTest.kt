package com.crowdproj.rating.mappers

import com.crowdproj.rating.api.v1.models.*
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.NONE
import com.crowdproj.rating.common.model.*
import com.crowdproj.rating.common.stub.CwpRatingStub
import kotlinx.datetime.Instant
import org.junit.Test
import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun fromTransport() {
        val req = RatingCreateRequest(
            requestId = "1",
            debug = RatingDebug(
                mode = RatingRequestDebugMode.STUB,
                stub = RatingRequestDebugStubs.SUCCESS,
            ),
            rating = RatingCreateObject(
                scoreTypeId = "11",
                objectId = "100",
                objectTypeId = "10",
            ),
        )

        val context = CwpRatingContext()
        context.fromTransport(req)

        assertEquals(CwpRatingStub.SUCCESS, context.stubCase)
        assertEquals(CwpRatingWorkMode.STUB, context.workMode)

        assertEquals("11", context.ratingRequest.scoreTypeId.asString())
        assertEquals("100", context.ratingRequest.objectId.asString())
        assertEquals("10", context.ratingRequest.objectTypeId.asString())
    }

    @Test
    fun toTransport() {
        val context = CwpRatingContext(
            requestId = CwpRatingRequestId("1"),
            command = CwpRatingCommand.CREATE,
            ratingResponse = CwpRating(
                scoreTypeId = CwpRatingScoreTypeId("11"),
                objectId = CwpRatingObjectId("100"),
                objectTypeId = CwpRatingObjectTypeId("10"),
                score = 3.5,
                voteCount = 200,
                createdAt = Instant.parse("2023-01-11T12:22:53Z"),
                updatedAt = Instant.NONE,
                ownerId = CwpRatingUserId("1")

            ),
            errors = mutableListOf(
                CwpRatingError(
                    code = "err",
                    group = "request",
                    field = "score",
                    title = "wrong value",
                    description = "wrong value",
                )
            ),
            state = CwpRatingState.RUNNING,
        )

        val req = context.toTransport() as RatingCreateResponse

        assertEquals("1", req.requestId)
        assertEquals("11", req.rating?.scoreTypeId)
        assertEquals("100", req.rating?.objectId)
        assertEquals("10", req.rating?.objectTypeId)
        assertEquals("3.5", req.rating?.score)
        assertEquals("200", req.rating?.voteCount)
        assertEquals("2023-01-11T12:22:53Z", req.rating?.createdAt)
        assertEquals("null", req.rating?.updatedAt)
        assertEquals("1", req.rating?.ownerId)

        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("score", req.errors?.firstOrNull()?.field)
        assertEquals("wrong value", req.errors?.firstOrNull()?.title)
        assertEquals("wrong value", req.errors?.firstOrNull()?.description)
    }
}