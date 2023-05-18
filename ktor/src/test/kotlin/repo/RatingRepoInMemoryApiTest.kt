package com.crowdproj.rating.ktor.repo

import com.crowdproj.rating.api.v1.models.*
import com.crowdproj.rating.common.CwpRatingCorSettings
import com.crowdproj.rating.common.model.*
import com.crowdproj.rating.ktor.CwpRatingAppSettings
import com.crowdproj.rating.ktor.module
import com.crowdproj.rating.repo.inmemory.RatingRepoInMemory
import com.crowdproj.rating.stubs.CwpRatingStub
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.kotlinModule
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class RatingRepoInMemoryApiTest {
    private val uuidOld = "10000000-0000-0000-0000-000000000001"
    private val uuidNew = "10000000-0000-0000-0000-000000000002"

    private val initRating = CwpRatingStub.prepareResult {
        id = CwpRatingId(uuidOld)
        scoreTypeId = CwpRatingScoreTypeId("1")
        objectTypeId = CwpRatingObjectTypeId("11")
        objectId = CwpRatingObjectId("111")
        lock = CwpRatingLock(uuidOld)
    }

    @Test
    fun create() = testApplication {
        val repo = RatingRepoInMemory(initObjects = listOf(initRating), randomUuid = { uuidNew })
        val settings = CwpRatingAppSettings(
            corSettings = CwpRatingCorSettings(repoTest = repo)
        )
        application { module(settings) }

        val client = myClient()

        val createRating = RatingCreateObject(
            scoreTypeId = "2",
            objectTypeId = "22",
            objectId = "222",
        )

        val response = client.post("api/v1/ratings/create") {
            val requestObj = RatingCreateRequest(
                requestId = "12345",
                rating = createRating,
                debug = RatingDebug(
                    mode = RatingRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<RatingCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(uuidNew, responseObj.rating?.id)
        assertEquals(createRating.scoreTypeId, responseObj.rating?.scoreTypeId)
        assertEquals(createRating.objectTypeId, responseObj.rating?.objectTypeId)
        assertEquals(createRating.objectId, responseObj.rating?.objectId)
    }

    @Test
    fun read() = testApplication {
        val repo = RatingRepoInMemory(initObjects = listOf(initRating), randomUuid = { uuidNew })
        application {
            module(CwpRatingAppSettings(corSettings = CwpRatingCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val response = client.post("api/v1/ratings/read") {
            val requestObj = RatingReadRequest(
                requestId = "12345",
                rating = RatingReadObject(uuidOld),
                debug = RatingDebug(
                    mode = RatingRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<RatingReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals(uuidOld, responseObj.rating?.id)
    }

    @Test
    fun update() = testApplication {
        val repo = RatingRepoInMemory(initObjects = listOf(initRating), randomUuid = { uuidNew })
        application {
            module(CwpRatingAppSettings(corSettings = CwpRatingCorSettings(repoTest = repo)))
        }
        val client = myClient()
        val ratingUpdate = RatingUpdateObject(
            id = uuidOld,
            scoreTypeId = "2",
            objectTypeId = "22",
            objectId = "222",
            lock = initRating.lock.asString(),
        )

        val response = client.post("api/v1/ratings/update") {
            val requestObj = RatingUpdateRequest(
                requestId = "12345",
                rating = ratingUpdate,
                debug = RatingDebug(
                    mode = RatingRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<RatingUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(ratingUpdate.id, responseObj.rating?.id)
        assertEquals(ratingUpdate.scoreTypeId, responseObj.rating?.scoreTypeId)
        assertEquals(ratingUpdate.objectTypeId, responseObj.rating?.objectTypeId)
        assertEquals(ratingUpdate.objectId, responseObj.rating?.objectId)
    }

    @Test
    fun delete() = testApplication {
        val repo = RatingRepoInMemory(initObjects = listOf(initRating), randomUuid = { uuidNew })
        application {
            module(CwpRatingAppSettings(corSettings = CwpRatingCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val response = client.post("api/v1/ratings/delete") {
            val requestObj = RatingDeleteRequest(
                requestId = "12345",
                rating = RatingDeleteObject(
                    id = uuidOld,
                    lock = initRating.lock.asString()
                ),
                debug = RatingDebug(
                    mode = RatingRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<RatingDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals(uuidOld, responseObj.rating?.id)
    }

    @Test
    fun search() = testApplication {
        val repo = RatingRepoInMemory(initObjects = listOf(initRating), randomUuid = { uuidNew })
        application {
            module(CwpRatingAppSettings(corSettings = CwpRatingCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val response = client.post("api/v1/ratings/search") {
            val requestObj = RatingSearchRequest(
                requestId = "12345",
                ratingFilter = RatingSearchFilter(),
                debug = RatingDebug(
                    mode = RatingRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<RatingSearchResponse>()
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.ratings?.size)
        assertEquals(uuidOld, responseObj.ratings?.first()?.id)
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