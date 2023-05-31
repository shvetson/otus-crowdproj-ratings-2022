package com.crowdproj.rating.repo.gremlin.mapper

import com.crowdproj.rating.common.model.CwpRating

fun CwpRating.label(): String? = this::class.simpleName
