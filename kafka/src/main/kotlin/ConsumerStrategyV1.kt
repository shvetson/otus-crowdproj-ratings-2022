package com.crowdproj.rating.kafka

import com.crowdproj.rating.api.apiV1RequestDeserialize
import com.crowdproj.rating.api.apiV1ResponseSerialize
import com.crowdproj.rating.api.v1.models.IRequest
import com.crowdproj.rating.api.v1.models.IResponse
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.mappers.fromTransport
import com.crowdproj.rating.mappers.toTransport

class ConsumerStrategyV1 : ConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)
    }

    override fun serialize(source: CwpRatingContext): String {
        val response: IResponse = source.toTransport()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: CwpRatingContext) {
        val request: IRequest = apiV1RequestDeserialize(value)
        target.fromTransport(request)
    }
}