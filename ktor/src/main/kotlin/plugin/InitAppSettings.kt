package com.crowdproj.rating.ktor.plugin

import com.crowdproj.rating.biz.CwpRatingProcessor
import com.crowdproj.rating.common.CwpRatingCorSettings
import com.crowdproj.rating.ktor.CwpRatingAppSettings
import com.crowdproj.rating.ktor.base.KtorAuthConfig
import com.crowdproj.rating.logging.common.CwpLoggerProvider
import com.crowdproj.rating.repo.stubs.RatingRepoStub
import io.ktor.server.application.*

fun Application.initAppSettings(): CwpRatingAppSettings {

    val corSettings = CwpRatingCorSettings(
        loggerProvider = getLoggerProviderConf(),
        repoTest = getDatabaseConf(RatingDbType.TEST),
        repoProd = getDatabaseConf(RatingDbType.PROD),
        repoStub = RatingRepoStub(),
    )

    return CwpRatingAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = CwpRatingProcessor(corSettings),
        auth = initAppAuth(),
    )
}

private fun Application.initAppAuth(): KtorAuthConfig = KtorAuthConfig(
    secret = environment.config.propertyOrNull("jwt.secret")?.getString() ?: "",
    issuer = environment.config.property("jwt.issuer").getString(),
    audience = environment.config.property("jwt.audience").getString(),
    realm = environment.config.property("jwt.realm").getString(),
    clientId = environment.config.property("jwt.clientId").getString(),
    certUrl = environment.config.propertyOrNull("jwt.certUrl")?.getString(),
)

//expect fun Application.getLoggerProviderConf(): CwpLoggerProvider