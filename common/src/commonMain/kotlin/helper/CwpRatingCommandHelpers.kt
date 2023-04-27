package com.crowdproj.rating.common.helpers

import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.CwpRatingCommand

fun CwpRatingContext.isUpdatableCommand() =
    this.command in listOf(CwpRatingCommand.CREATE, CwpRatingCommand.UPDATE, CwpRatingCommand.DELETE)
