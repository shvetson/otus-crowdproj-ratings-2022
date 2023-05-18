package com.crowdproj.rating.ktor.stubs

import com.crowdproj.rating.api.v1.models.*
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class RatingStubApiTest {

    @Test
    fun create() = testApplication {
        val client = myClient()

        val response = client.post("/api/v1/ratings/create") {
            val requestObj = RatingCreateRequest(
                requestType = "create",
                requestId = "1",
                debug = RatingDebug(
                    mode = RatingRequestDebugMode.STUB,
                    stub = RatingRequestDebugStubs.SUCCESS
                ),
                rating = RatingCreateObject(
                    scoreTypeId = "1",
                    objectId = "100",
                    objectTypeId = "10",
                ),
            )

            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<RatingCreateResponse>()

        assertEquals(200, response.status.value)
        assertEquals("111", responseObj.rating?.id)
    }

    @Test
    fun read() = testApplication {
        val client = myClient()

        val response = client.post("/api/v1/ratings/read") {
            val requestObj = RatingReadRequest(
                requestType = "read",
                requestId = "1",
                debug = RatingDebug(
                    mode = RatingRequestDebugMode.STUB,
                    stub = RatingRequestDebugStubs.SUCCESS
                ),
                rating = RatingReadObject("111"),
            )

            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<RatingReadResponse>()

        assertEquals(200, response.status.value)
        assertEquals("111", responseObj.rating?.id)
    }

    @Test
    fun update() = testApplication {
        val client = myClient()

        val response = client.post("/api/v1/ratings/update") {
            val requestObj = RatingUpdateRequest(
                requestType = "update",
                requestId = "1",
                debug = RatingDebug(
                    mode = RatingRequestDebugMode.STUB,
                    stub = RatingRequestDebugStubs.SUCCESS
                ),
                rating = RatingUpdateObject(
                    scoreTypeId = "1",
                    objectId = "100",
                    objectTypeId = "10",
                    id = "111",
                    lock = null
                ),
            )

            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<RatingUpdateResponse>()

        assertEquals(200, response.status.value)
        assertEquals("111", responseObj.rating?.id)
    }

    @Test
    fun delete() = testApplication {
        val client = myClient()

        val response = client.post("/api/v1/ratings/delete") {
            val requestObj = RatingDeleteRequest(
                requestType = "delete",
                requestId = "1",
                debug = RatingDebug(
                    mode = RatingRequestDebugMode.STUB,
                    stub = RatingRequestDebugStubs.SUCCESS
                ),
                rating = RatingDeleteObject("111"),
            )

            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<RatingDeleteResponse>()
        println(responseObj)

        assertEquals(200, response.status.value)
        assertEquals("111", responseObj.rating?.id)
    }

    @Test
    fun search() = testApplication {
        val client = myClient()

        val response = client.post("/api/v1/ratings/search") {
            val requestObj = RatingSearchRequest(
                requestId = "1",
                debug = RatingDebug(
                    mode = RatingRequestDebugMode.STUB,
                    stub = RatingRequestDebugStubs.SUCCESS
                ),
                ratingFilter = RatingSearchFilter(),
            )

            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<RatingSearchResponse>()

        assertEquals(200, response.status.value)
        assertEquals("pr-111-01", responseObj.ratings?.first()?.id)
    }

    private fun ApplicationTestBuilder.myClient() = createClient {

        install(ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                enable(SerializationFeature.INDENT_OUTPUT)
                writerWithDefaultPrettyPrinter()
            }
        }
    }
}
