package com.crowdproj.rating.ktor.controller

import com.crowdproj.rating.api.v1.models.IRequest
import com.crowdproj.rating.api.v1.models.IResponse
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.helper.asCwpRatingError
import com.crowdproj.rating.common.model.CwpRatingCommand
import com.crowdproj.rating.common.model.CwpRatingState
import com.crowdproj.rating.ktor.CwpRatingAppSettings
import com.crowdproj.rating.logging.common.ICwpLogWrapper
import com.crowdproj.rating.mappers.fromTransport
import com.crowdproj.rating.mappers.log.toLog
import com.crowdproj.rating.mappers.toTransport
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.datetime.Clock

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processV1(
    appSettings: CwpRatingAppSettings,
    logger: ICwpLogWrapper,
    logId: String,
    command: CwpRatingCommand? = null,
) {
    val ctx = CwpRatingContext(timeStart = Clock.System.now(),)
    val processor = appSettings.processor

    try {
        logger.doWithLogging(id = logId) {
            val request = receive<Q>()
            ctx.fromTransport(request)
            logger.info(
                msg = "$command request is got",
                data = ctx.toLog("${logId}-got")
            )
            processor.exec(ctx)
            logger.info(
                msg = "$command request is handled",
                data = ctx.toLog("${logId}-handled")
            )
            respond(ctx.toTransport())
        }
    } catch (e: Throwable) {
        logger.doWithLogging(id = "${logId}-failure") {
            command?.also { ctx.command = it }
            logger.error( msg = "$command handling failed",)
            ctx.state = CwpRatingState.FINISHING
            ctx.errors.add(e.asCwpRatingError())
            processor.exec(ctx)
            respond(ctx.toTransport())
        }
    }
}
