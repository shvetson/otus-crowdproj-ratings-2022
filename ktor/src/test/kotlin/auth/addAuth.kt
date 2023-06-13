package com.crowdproj.rating.ktor.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.crowdproj.rating.ktor.base.KtorAuthConfig
import io.ktor.client.request.*
import io.ktor.http.*

fun HttpRequestBuilder.addAuth(
    id: String? = null,
    groups: List<String>? = null,
    config: KtorAuthConfig
) {
    val token = JWT.create()
        .withAudience(config.audience)
        .withIssuer(config.issuer)
        .withClaim(KtorAuthConfig.GROUPS_CLAIM, groups)
        .withClaim(KtorAuthConfig.ID_CLAIM, id)
        .sign(Algorithm.HMAC256(config.secret))

    header(HttpHeaders.Authorization, "Bearer $token")
}