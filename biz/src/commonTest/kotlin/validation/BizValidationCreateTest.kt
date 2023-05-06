package com.crowdproj.rating.biz.validation

import com.crowdproj.rating.biz.CwpRatingProcessor
import com.crowdproj.rating.common.model.CwpRatingCommand
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationCreateTest {

    private val command = CwpRatingCommand.CREATE
    private val processor by lazy { CwpRatingProcessor() }

    @Test fun correctScoreTypeId() = validationScoreTypeIdCorrect(command, processor)
    @Test fun trimScoreTypeId() = validationScoreTypeIdTrim(command, processor)
    @Test fun emptyScoreTypeId() = validationScoreTypeIdEmpty(command, processor)
    @Test fun badFormatScoreTypeId() = validationScoreTypeIdFormat(command, processor)

    @Test fun correctObjectId() = validationObjectIdCorrect(command, processor)
    @Test fun trimObjectId() = validationObjectIdTrim(command, processor)
    @Test fun emptyObjectId() = validationObjectIdEmpty(command, processor)
    @Test fun badFormatObjectId() = validationObjectIdFormat(command, processor)

    @Test fun correctObjectTypeId() = validationObjectTypeIdCorrect(command, processor)
    @Test fun trimObjectTypeId() = validationObjectTypeIdTrim(command, processor)
    @Test fun emptyObjectTypeId() = validationObjectTypeIdEmpty(command, processor)
    @Test fun badFormatObjectTypeId() = validationObjectTypeIdFormat(command, processor)
}