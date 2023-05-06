package com.crowdproj.rating.biz.validation

import com.crowdproj.rating.biz.CwpRatingProcessor
import com.crowdproj.rating.common.model.CwpRatingCommand
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationReadTest {

    private val command = CwpRatingCommand.READ
    private val processor by lazy { CwpRatingProcessor() }

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)
}