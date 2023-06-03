package com.crowdproj.rating.auth

import com.crowdproj.rating.common.model.CwpRatingPermission
import com.crowdproj.rating.common.permission.CwpRatingPermissionClient
import com.crowdproj.rating.common.permission.CwpRatingPrincipalRelations
import com.crowdproj.rating.common.permission.CwpRatingUserPermissions

fun resolveFrontPermissions(
    permissions: Iterable<CwpRatingUserPermissions>,
    relations: Iterable<CwpRatingPrincipalRelations>,
) = mutableSetOf<CwpRatingPermission>()
    .apply {
        for (permission in permissions) {
            for (relation in relations) {
                accessTable[permission]?.get(relation)?.let { this@apply.add(it) }
            }
        }
    }
    .toSet()

private val accessTable = mapOf(
    // READ
    CwpRatingUserPermissions.READ_OWN to mapOf(
        CwpRatingPrincipalRelations.OWN to CwpRatingPermission.READ
    ),
    CwpRatingUserPermissions.READ_GROUP to mapOf(
        CwpRatingPrincipalRelations.GROUP to CwpRatingPermission.READ
    ),
    CwpRatingUserPermissions.READ_PUBLIC to mapOf(
        CwpRatingPrincipalRelations.PUBLIC to CwpRatingPermission.READ
    ),
    CwpRatingUserPermissions.READ_CANDIDATE to mapOf(
        CwpRatingPrincipalRelations.MODERATABLE to CwpRatingPermission.READ
    ),

    // UPDATE
    CwpRatingUserPermissions.UPDATE_OWN to mapOf(
        CwpRatingPrincipalRelations.OWN to CwpRatingPermission.UPDATE
    ),
    CwpRatingUserPermissions.UPDATE_PUBLIC to mapOf(
        CwpRatingPrincipalRelations.MODERATABLE to CwpRatingPermission.UPDATE
    ),
    CwpRatingUserPermissions.UPDATE_CANDIDATE to mapOf(
        CwpRatingPrincipalRelations.MODERATABLE to CwpRatingPermission.UPDATE
    ),

    // DELETE
    CwpRatingUserPermissions.DELETE_OWN to mapOf(
        CwpRatingPrincipalRelations.OWN to CwpRatingPermission.DELETE
    ),
    CwpRatingUserPermissions.DELETE_PUBLIC to mapOf(
        CwpRatingPrincipalRelations.MODERATABLE to CwpRatingPermission.DELETE
    ),
    CwpRatingUserPermissions.DELETE_CANDIDATE to mapOf(
        CwpRatingPrincipalRelations.MODERATABLE to CwpRatingPermission.DELETE
    ),
)