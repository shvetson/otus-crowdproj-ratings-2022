package com.crowdproj.rating.spring.service

import com.crowdproj.rating.biz.CwpRatingProcessor
import com.crowdproj.rating.common.CwpRatingContext
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class CwpRatingBlockingProcessor {
    private val processor = CwpRatingProcessor()

    fun exec(ctx: CwpRatingContext) = runBlocking { processor.exec(ctx) }
}