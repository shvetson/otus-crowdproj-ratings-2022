package com.crowdproj.rating.auth

import com.crowdproj.rating.common.permission.CwpRatingUserGroups
import com.crowdproj.rating.common.permission.CwpRatingUserPermissions

fun resolveChainPermissions(groups: Iterable<CwpRatingUserGroups>) =
    mutableSetOf<CwpRatingUserPermissions>()
        .apply {
            addAll(groups.flatMap { groupPermissionsAdmits[it] ?: emptySet() })
            removeAll(groups.flatMap { groupPermissionsDenys[it] ?: emptySet() }.toSet())
        }
        .toSet()

private val groupPermissionsAdmits = mapOf(
    CwpRatingUserGroups.USER to setOf(
        CwpRatingUserPermissions.READ_OWN,
        CwpRatingUserPermissions.READ_PUBLIC,
        CwpRatingUserPermissions.CREATE_OWN,
        CwpRatingUserPermissions.UPDATE_OWN,
        CwpRatingUserPermissions.DELETE_OWN,
    ),
    CwpRatingUserGroups.MODERATOR_CWP to setOf(),
    CwpRatingUserGroups.ADMIN_RATINGS to setOf(),
    CwpRatingUserGroups.TEST to setOf(),
    CwpRatingUserGroups.BAN_RATINGS to setOf(),
)

private val groupPermissionsDenys = mapOf(
    CwpRatingUserGroups.USER to setOf(),
    CwpRatingUserGroups.MODERATOR_CWP to setOf(),
    CwpRatingUserGroups.ADMIN_RATINGS to setOf(),
    CwpRatingUserGroups.TEST to setOf(),
    CwpRatingUserGroups.BAN_RATINGS to setOf(
        CwpRatingUserPermissions.UPDATE_OWN,
        CwpRatingUserPermissions.CREATE_OWN,
    ),
)