package com.crowdproj.rating.ktor.plugin

import com.auth0.jwt.JWT
import com.crowdproj.rating.ktor.CwpRatingAppSettings
import com.crowdproj.rating.ktor.base.KtorAuthConfig.Companion.GROUPS_CLAIM
import com.crowdproj.rating.ktor.base.resolveAlgorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureAuth(appSettings: CwpRatingAppSettings) {

    install(Authentication) {
        jwt("auth-jwt") {
            val authConfig = appSettings.auth
            realm = authConfig.realm

            verifier {
                val algorithm = it.resolveAlgorithm(authConfig)
                JWT
                    .require(algorithm)
                    .withAudience(authConfig.audience)
                    .withIssuer(authConfig.issuer)
                    .build()
            }
            validate { jwtCredential: JWTCredential ->
                when {
                    jwtCredential.payload.getClaim(GROUPS_CLAIM).asList(String::class.java).isNullOrEmpty() -> {
                        this@configureAuth.log.error("Groups claim must not be empty in JWT token")
                        null
                    }

                    else -> JWTPrincipal(jwtCredential.payload)
                }
            }
        }
    }
}