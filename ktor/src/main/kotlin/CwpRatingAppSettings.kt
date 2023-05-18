package com.crowdproj.rating.ktor

import com.crowdproj.rating.biz.CwpRatingProcessor
import com.crowdproj.rating.common.CwpRatingCorSettings

data class CwpRatingAppSettings(
    val appUrls: List<String> = emptyList(),
    val corSettings: CwpRatingCorSettings,
    val processor: CwpRatingProcessor = CwpRatingProcessor(settings = corSettings),
)