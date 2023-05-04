package com.crowdproj.rating.biz.stub

import com.crowdproj.rating.biz.CwpRatingProcessor
import com.crowdproj.rating.common.model.CwpRatingCommand
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationCreateTest {

    private val command = CwpRatingCommand.CREATE
    private val processor by lazy { CwpRatingProcessor() }

//    @Test fun correctTitle() = validationTitleCorrect(command, processor)
//    @Test fun trimTitle() = validationTitleTrim(command, processor)
//    @Test fun emptyTitle() = validationTitleEmpty(command, processor)
//    @Test fun badSymbolsTitle() = validationTitleSymbols(command, processor)
//
//    @Test fun correctDescription() = validationDescriptionCorrect(command, processor)
//    @Test fun trimDescription() = validationDescriptionTrim(command, processor)
//    @Test fun emptyDescription() = validationDescriptionEmpty(command, processor)
//    @Test fun badSymbolsDescription() = validationDescriptionSymbols(command, processor)

}

