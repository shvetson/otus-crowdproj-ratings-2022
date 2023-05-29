package com.crowdproj.rating.common.helper

import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingCommand

fun CwpRatingContext.isUpdatableCommand() =
    this.command in listOf(CwpRatingCommand.CREATE, CwpRatingCommand.UPDATE, CwpRatingCommand.DELETE)
