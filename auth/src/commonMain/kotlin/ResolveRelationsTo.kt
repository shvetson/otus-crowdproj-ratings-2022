package com.crowdproj.rating.auth

import com.crowdproj.rating.common.model.CwpRating
import com.crowdproj.rating.common.model.CwpRatingId
import com.crowdproj.rating.common.model.CwpRatingVisibility
import com.crowdproj.rating.common.permission.CwpRatingPrincipalModel
import com.crowdproj.rating.common.permission.CwpRatingPrincipalRelations

fun CwpRating.resolveRelationsTo(principal: CwpRatingPrincipalModel): Set<CwpRatingPrincipalRelations> =
    setOfNotNull(
        CwpRatingPrincipalRelations.NONE,
        CwpRatingPrincipalRelations.NEW.takeIf { id == CwpRatingId.NONE },
        CwpRatingPrincipalRelations.OWN.takeIf { principal.id == ownerId },
        CwpRatingPrincipalRelations.MODERATABLE.takeIf { visibility != CwpRatingVisibility.VISIBLE_TO_OWNER },
        CwpRatingPrincipalRelations.PUBLIC.takeIf { visibility == CwpRatingVisibility.VISIBLE_PUBLIC },
    )