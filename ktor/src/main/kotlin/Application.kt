package com.crowdproj.rating.ktor

import com.crowdproj.rating.ktor.plugin.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.request.*

import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main(args: Array<String>): Unit =
    io.ktor.server.cio.EngineMain.main(args)

private val clazz = Application::module::class.qualifiedName ?: "Application"
@Suppress("unused")
fun Application.module(appSettings: CwpRatingAppSettings = initAppSettings()) {
    configureSerialization()
    configureAuth(appSettings)
    configureRouting(appSettings)
    configureCallLogging(appSettings, clazz)
}