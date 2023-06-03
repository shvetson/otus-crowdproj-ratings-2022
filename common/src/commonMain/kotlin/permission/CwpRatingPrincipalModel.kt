package com.crowdproj.rating.common.permission

import com.crowdproj.rating.common.model.CwpRatingUserId

data class CwpRatingPrincipalModel(
    val id: CwpRatingUserId = CwpRatingUserId.NONE,
    val fname: String = "",
    val mname: String = "",
    val lname: String = "",
    val groups: Set<CwpRatingUserGroups> = emptySet()
) {
    companion object {
        val NONE = CwpRatingPrincipalModel()
    }
}