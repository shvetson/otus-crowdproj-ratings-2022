package com.crowdproj.rating.rabbit

import com.crowdproj.rating.api.apiV1Mapper
import com.crowdproj.rating.api.v1.models.*
import com.crowdproj.rating.rabbit.config.RabbitConfig
import com.crowdproj.rating.rabbit.config.RabbitExchangeConfiguration
import com.crowdproj.rating.rabbit.controller.RabbitController
import com.crowdproj.rating.rabbit.processor.RabbitDirectProcessorV1
import com.crowdproj.rating.stubs.CwpRatingStub
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.testcontainers.containers.RabbitMQContainer
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

// тесты с использованием testcontainers
internal class RabbitMqTest {

    companion object {
        const val exchange = "test-exchange"
        const val exchangeType = "direct"
    }

    val container by lazy {
//            Этот образ предназначен для дебагинга, он содержит панель управления на порту httpPort
//            RabbitMQContainer("rabbitmq:3-management").apply {
//            Этот образ минимальный и не содержит панель управления
        RabbitMQContainer("rabbitmq:latest").apply {
            withExposedPorts(5672, 15672)
            withUser("guest", "guest")
            start()
        }
    }

    val rabbitMqTestPort: Int by lazy {
        container.getMappedPort(5672)
    }
    val config by lazy {
        RabbitConfig(
            port = rabbitMqTestPort
        )
    }
    val processorV1 by lazy {
        RabbitDirectProcessorV1(
            config = config,
            processorConfig = RabbitExchangeConfiguration(
                keyIn = "in-v1",
                keyOut = "out-v1",
                exchange = exchange,
                queue = "v1-queue",
                consumerTag = "test-tag",
                exchangeType = exchangeType
            )
        )
    }

    val controller by lazy {
        RabbitController(
            processors = setOf(processorV1)
        )
    }

    @BeforeTest
    fun tearUp() {
        controller.start()
    }

    @Test
    fun ratingCreateTestV1() {
        val keyOut = processorV1.processorConfig.keyOut
        val keyIn = processorV1.processorConfig.keyIn
        ConnectionFactory().apply {
            host = config.host
            port = config.port
            username = "guest"
            password = "guest"
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(exchange, "direct")
                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, exchange, keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

                channel.basicPublish(exchange, keyIn, null, apiV1Mapper.writeValueAsBytes(ratingCreateV1))

                runBlocking {
                    withTimeoutOrNull(265L) {
                        while (responseJson.isBlank()) {
                            delay(10)
                        }
                    }
                }

                println("RESPONSE: $responseJson")
                val response = apiV1Mapper.readValue(responseJson, RatingCreateResponse::class.java)
                val expected = CwpRatingStub.get()

                assertEquals(expected.objectId.asString(), response.rating?.objectId)
                assertEquals(expected.scoreTypeId.asString(), response.rating?.scoreTypeId)
            }
        }
    }

    private val ratingCreateV1 = with(CwpRatingStub.get()) {
        RatingCreateRequest(
            rating = RatingCreateObject(
                scoreTypeId = "11",
                objectId = "100",
                objectTypeId = "10",
            ),
            requestType = "create",
            debug = RatingDebug(
                mode = RatingRequestDebugMode.STUB,
                stub = RatingRequestDebugStubs.SUCCESS
            )
        )
    }
}
