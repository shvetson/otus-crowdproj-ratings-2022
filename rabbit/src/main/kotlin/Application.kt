package com.crowdproj.rating.rabbit

import com.crowdproj.rating.biz.CwpRatingProcessor
import com.crowdproj.rating.rabbit.config.RabbitConfig
import com.crowdproj.rating.rabbit.config.RabbitExchangeConfiguration
import com.crowdproj.rating.rabbit.controller.RabbitController
import com.crowdproj.rating.rabbit.processor.RabbitDirectProcessorV1

// настройки приложения
fun main() {
    val config = RabbitConfig()
    val ratingProcessor = CwpRatingProcessor()

    val producerConfigV1 = RabbitExchangeConfiguration(
        keyIn = "in-v1",
        keyOut = "out-v1",
        exchange = "transport-exchange",
        queue = "v1-queue",
        consumerTag = "v1-consumer",
        exchangeType = "direct"
    )

    val processor by lazy {
        RabbitDirectProcessorV1(
            config = config,
            processorConfig = producerConfigV1,
            processor = ratingProcessor
        )
    }

    val controller by lazy {
        RabbitController(
            processors = setOf(processor)
        )
    }
    controller.start()
}
