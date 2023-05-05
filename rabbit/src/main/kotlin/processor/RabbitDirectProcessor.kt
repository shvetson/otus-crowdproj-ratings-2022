package com.crowdproj.rating.rabbit.processor

import com.crowdproj.rating.api.apiV1Mapper
import com.crowdproj.rating.api.v1.models.IRequest
import com.crowdproj.rating.biz.CwpRatingProcessor
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.helper.addError
import com.crowdproj.rating.common.helper.asCwpRatingError
import com.crowdproj.rating.common.model.CwpRatingState
import com.crowdproj.rating.mappers.fromTransport
import com.crowdproj.rating.mappers.toTransport
import com.crowdproj.rating.rabbit.RabbitProcessorBase
import com.crowdproj.rating.rabbit.config.RabbitConfig
import com.crowdproj.rating.rabbit.config.RabbitExchangeConfiguration
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import kotlinx.datetime.Clock

// наследник RabbitProcessorBase, увязывает транспортную и бизнес-части
class RabbitDirectProcessorV1(
    config: RabbitConfig,
    processorConfig: RabbitExchangeConfiguration,
    private val processor: CwpRatingProcessor = CwpRatingProcessor(),
) : RabbitProcessorBase(config, processorConfig) {

    private val ctx = CwpRatingContext()

    override suspend fun Channel.processMessage(message: Delivery) {
        ctx.apply {
            timeStart = Clock.System.now()
        }

        apiV1Mapper.readValue(message.body, IRequest::class.java).run {
            ctx.fromTransport(this).also {
                println("TYPE: ${this::class.simpleName}")
            }
        }
        val response = processor.exec(ctx).run { ctx.toTransport() }
        apiV1Mapper.writeValueAsBytes(response).also {
            println("Publishing $response to ${processorConfig.exchange} exchange for keyOut ${processorConfig.keyOut}")
            basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it)
        }.also {
            println("published")
        }
    }

    override fun Channel.onError(e: Throwable) {
        e.printStackTrace()
        ctx.state = CwpRatingState.NONE
        ctx.addError(error = arrayOf(e.asCwpRatingError()))
        val response = ctx.toTransport()
        apiV1Mapper.writeValueAsBytes(response).also {
            basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it)
        }
    }
}
