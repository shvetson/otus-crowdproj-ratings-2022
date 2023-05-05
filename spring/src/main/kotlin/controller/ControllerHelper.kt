package com.crowdproj.rating.spring.controller

import com.crowdproj.rating.api.v1.models.IRequest
import com.crowdproj.rating.api.v1.models.IResponse
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.helper.asCwpRatingError
import com.crowdproj.rating.common.model.CwpRatingCommand
import com.crowdproj.rating.common.model.CwpRatingState
import com.crowdproj.rating.logging.common.ICwpLogWrapper
import com.crowdproj.rating.mappers.fromTransport
import com.crowdproj.rating.mappers.log.toLog
import com.crowdproj.rating.mappers.toTransport
import com.crowdproj.rating.spring.CwpRatingBlockingProcessor
import kotlinx.datetime.Clock

suspend inline fun <reified Q : IRequest, reified R : IResponse> processV1(
    processor: CwpRatingBlockingProcessor,
    command: CwpRatingCommand? = null,
    request: Q,
    logger: ICwpLogWrapper,
    logId: String,
): R {
    val ctx = CwpRatingContext(
        timeStart = Clock.System.now(),
    )
    return try {
        logger.doWithLogging(id = logId) {
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
            ctx.toTransport() as R
        }
    } catch (e: Throwable) {
        logger.doWithLogging(id = "${logId}-failure") {
            command?.also { ctx.command = it }
            logger.error(
                msg = "$command handling failed",
            )
            ctx.state = CwpRatingState.FINISHING
            ctx.errors.add(e.asCwpRatingError())
            processor.exec(ctx)
            ctx.toTransport() as R
        }
    }
}