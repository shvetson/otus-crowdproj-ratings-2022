package com.crowdproj.rating.ktor.auth

import com.crowdproj.rating.ktor.base.KtorAuthConfig
import com.crowdproj.rating.ktor.helper.testSettings
import com.crowdproj.rating.ktor.module
import io.ktor.client.request.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class AuthTest {
    @Test
    fun invalidAudience() = testApplication {
        val response = client.post("/api/v1/ratings/create") {
            addAuth(
                id = "user1",
                groups = listOf("USER", "TEST"),
                config = KtorAuthConfig.TEST.copy(audience = "invalid")
            )
        }
        assertEquals(401, response.status.value)
    }
}