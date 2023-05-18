package com.crowdproj.rating.common.repo

import com.crowdproj.rating.common.model.CwpRatingObjectId
import com.crowdproj.rating.common.model.CwpRatingObjectTypeId
import com.crowdproj.rating.common.model.CwpRatingScoreTypeId
import com.crowdproj.rating.common.model.CwpRatingUserId

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  13.05.2023 23:19
 */

data class DbRatingFilterRequest(
    val scoreTypeId: CwpRatingScoreTypeId = CwpRatingScoreTypeId.NONE,
    val objectTypeId: CwpRatingObjectTypeId = CwpRatingObjectTypeId.NONE,
    val objectId: CwpRatingObjectId = CwpRatingObjectId.NONE,
    val ownerId: CwpRatingUserId = CwpRatingUserId.NONE,
)