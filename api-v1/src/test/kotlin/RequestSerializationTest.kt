package com.crowdproj.rating.api

import com.crowdproj.rating.api.v1.models.*
import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestSerializationTest {

    private val request = RatingCreateRequest(
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

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"scoreTypeId\":\\s*\"11\""))
        assertContains(json, Regex("\"objectId\":\\s*\"100\""))
        assertContains(json, Regex("\"objectTypeId\":\\s*\"10\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as RatingCreateRequest

        assertEquals(request, obj)
    }
}