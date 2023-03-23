package com.crowdproj.rating.mappers

import com.crowdproj.rating.api.v1.models.*
import com.crowdproj.rating.common.CwpRatContext
import com.crowdproj.rating.common.NONE
import com.crowdproj.rating.common.model.*
import kotlinx.datetime.Instant
import org.junit.Test
import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun fromTransport() {
        val req = RatingCreateRequest(
            requestId = "1234",
            debug = RatingDebug(
                mode = RatingRequestDebugMode.STUB,
                stub = RatingRequestDebugStubs.SUCCESS,
            ),
            rating = RatingCreateObject(
                scoreTypeId = "1",
                objectId = "10",
                objectTypeId = "11",
            ),
        )

        val context = CwpRatContext()
        context.fromTransport(req)

        assertEquals(CwpRatStub.SUCCESS, context.stubCase)
        assertEquals(CwpRatWorkMode.STUB, context.workMode)

        assertEquals("1", context.ratingRequest.scoreTypeId.asString())
        assertEquals("10", context.ratingRequest.objectId.asString())
        assertEquals("11", context.ratingRequest.objectTypeId.asString())
    }

    @Test
    fun toTransport() {
        val context = CwpRatContext(
            requestId = CwpRatRequestId("1234"),
            command = CwpRatCommand.CREATE,
            ratingResponse = CwpRat(
                scoreTypeId = CwpRatScoreTypeId("1"),
                objectId = CwpRatObjectId("10"),
                objectTypeId = CwpRatObjectTypeId("11"),
                score = "5",
                voteCount = "100",
                createdAt = Instant.parse("2023-01-11T12:22:53Z"),
                updatedAt = Instant.NONE,
                ownerId = CwpRatUserId("1000")

            ),
            errors = mutableListOf(
                CwpRatError(
                    code = "err",
                    group = "request",
                    field = "score",
                    title = "wrong value",
                    description = "wrong value",
                )
            ),
            state = CwpRatState.RUNNING,
        )

        val req = context.toTransport() as RatingCreateResponse

        assertEquals("1234", req.requestId)
        assertEquals("1", req.rating?.scoreTypeId)
        assertEquals("10", req.rating?.objectId)
        assertEquals("11", req.rating?.objectTypeId)
        assertEquals("5", req.rating?.score)
        assertEquals("100", req.rating?.voteCount)
        assertEquals("2023-01-11T12:22:53Z", req.rating?.createdAt)
        assertEquals("null", req.rating?.updatedAt)
        assertEquals("1000", req.rating?.ownerId)

        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("score", req.errors?.firstOrNull()?.field)
        assertEquals("wrong value", req.errors?.firstOrNull()?.title)
        assertEquals("wrong value", req.errors?.firstOrNull()?.description)
    }
}