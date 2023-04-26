package com.crowdproj.rating.kafka

import com.crowdproj.rating.api.apiV1RequestSerialize
import com.crowdproj.rating.api.apiV1ResponseDeserialize
import com.crowdproj.rating.api.v1.models.*
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class KafkaControllerTest {
    @Test
    fun runKafka() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = AppKafkaConsumer(config, listOf(ConsumerStrategyV1()), consumer = consumer, producer = producer)
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiV1RequestSerialize(
                        RatingCreateRequest(
                            requestId = "11111111-1111-1111-1111-111111111111",
                            rating = RatingCreateObject(
                                scoreTypeId = "1",
                                objectId = "100",
                                objectTypeId = "10",
                            ),
                            debug = RatingDebug(
                                mode = RatingRequestDebugMode.STUB,
                                stub = RatingRequestDebugStubs.SUCCESS
                            )
                        )
                    )
                )
            )
            app.stop()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.run()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize<RatingCreateResponse>(message.value())
        assertEquals(outputTopic, message.topic())
        assertEquals("11111111-1111-1111-1111-111111111111", result.requestId)
        assertEquals("11", result.rating?.scoreTypeId)
    }

    companion object {
        const val PARTITION = 0
    }
}


