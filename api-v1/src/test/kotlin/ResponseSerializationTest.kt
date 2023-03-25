package com.crowdproj.rating.api

import com.crowdproj.rating.api.v1.models.IResponse
import com.crowdproj.rating.api.v1.models.RatingCreateResponse
import com.crowdproj.rating.api.v1.models.RatingResponseObject
import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseSerializationTest {

    private val response = RatingCreateResponse(
        requestId = "1",
        rating = RatingResponseObject(
            scoreTypeId = "11",
            objectId = "100",
            objectTypeId = "10",
            score = "3.5",
            voteCount = "200",
            createdAt = "2023-01-11T12:22:53Z",
            updatedAt = "2023-01-11T12:22:53Z",
            ownerId = "1",
        ),
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"scoreTypeId\":\\s*\"11\""))
        assertContains(json, Regex("\"objectId\":\\s*\"100\""))
        assertContains(json, Regex("\"objectTypeId\":\\s*\"10\""))
        assertContains(json, Regex("\"score\":\\s*\"3.5\""))
        assertContains(json, Regex("\"voteCount\":\\s*\"200\""))
        assertContains(json, Regex("\"createdAt\":\\s*\"2023-01-11T12:22:53Z\""))
        assertContains(json, Regex("\"updatedAt\":\\s*\"2023-01-11T12:22:53Z\""))
        assertContains(json, Regex("\"ownerId\":\\s*\"1\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as RatingCreateResponse
        assertEquals(response, obj)
    }
}