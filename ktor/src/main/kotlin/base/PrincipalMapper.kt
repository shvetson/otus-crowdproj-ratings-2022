package com.crowdproj.rating.ktor.base

import com.crowdproj.rating.common.model.CwpRatingUserId
import com.crowdproj.rating.common.permission.CwpRatingPrincipalModel
import com.crowdproj.rating.common.permission.CwpRatingUserGroups
import com.crowdproj.rating.ktor.base.KtorAuthConfig.Companion.F_NAME_CLAIM
import com.crowdproj.rating.ktor.base.KtorAuthConfig.Companion.GROUPS_CLAIM
import com.crowdproj.rating.ktor.base.KtorAuthConfig.Companion.ID_CLAIM
import com.crowdproj.rating.ktor.base.KtorAuthConfig.Companion.L_NAME_CLAIM
import com.crowdproj.rating.ktor.base.KtorAuthConfig.Companion.M_NAME_CLAIM
import io.ktor.server.auth.jwt.*

fun JWTPrincipal?.toModel() = this?.run {
    CwpRatingPrincipalModel(
        id = payload.getClaim(ID_CLAIM).asString()?.let { CwpRatingUserId(it) } ?: CwpRatingUserId.NONE,
        fname = payload.getClaim(F_NAME_CLAIM).asString() ?: "",
        mname = payload.getClaim(M_NAME_CLAIM).asString() ?: "",
        lname = payload.getClaim(L_NAME_CLAIM).asString() ?: "",
        groups = payload
            .getClaim(GROUPS_CLAIM)
            ?.asList(String::class.java)
            ?.mapNotNull {
                when(it) {
                    "USER" -> CwpRatingUserGroups.USER
                    else -> null
                }
            }?.toSet() ?: emptySet()
    )
} ?: CwpRatingPrincipalModel.NONE