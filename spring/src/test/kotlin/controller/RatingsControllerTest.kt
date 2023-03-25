package com.crowdproj.rating.spring.controller

import com.crowdproj.rating.api.v1.models.*
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.mappers.*
import com.crowdproj.rating.stubs.CwpRatingStub
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

// Temporary simple test with stubs
@WebMvcTest(RatingsController::class)
internal class RatingsControllerTest {
    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Test
    fun createRating() = testStubRating(
        "/v1/ratings/create",
        RatingCreateRequest(),
        CwpRatingContext().apply { ratingResponse = CwpRatingStub.get() }.toTransportCreate()
    )

    @Test
    fun readRating() = testStubRating(
        "/v1/ratings/read",
        RatingReadRequest(),
        CwpRatingContext().apply { ratingResponse = CwpRatingStub.get() }.toTransportRead()
    )

    @Test
    fun updateRating() = testStubRating(
        "/v1/ratings/update",
        RatingUpdateRequest(),
        CwpRatingContext().apply { ratingResponse = CwpRatingStub.get() }.toTransportUpdate()
    )

    @Test
    fun deleteRating() = testStubRating(
        "/v1/ratings/delete",
        RatingDeleteRequest(),
        CwpRatingContext().toTransportDelete()
    )

    @Test
    fun searchRating() = testStubRating(
        "/v1/ratings/search",
        RatingSearchRequest(),
        CwpRatingContext().apply { ratingsResponse.add(CwpRatingStub.get()) }.toTransportSearch()
    )

    private fun <Req : Any, Res : Any> testStubRating(
        url: String,
        requestObj: Req,
        responseObj: Res,
    ) {
        val request = mapper.writeValueAsString(requestObj)
        val response = mapper.writeValueAsString(responseObj)

        mvc.perform(
            post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        )
            .andExpect(status().isOk)
            .andExpect(content().json(response))
    }
}
