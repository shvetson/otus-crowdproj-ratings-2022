package com.crowdproj.rating.auth

import com.crowdproj.rating.common.model.CwpRatingCommand
import com.crowdproj.rating.common.permission.CwpRatingPrincipalRelations
import com.crowdproj.rating.common.permission.CwpRatingUserPermissions

fun checkPermitted(
    command: CwpRatingCommand,
    relations: Iterable<CwpRatingPrincipalRelations>,
    permissions: Iterable<CwpRatingUserPermissions>,
) =
    relations.asSequence().flatMap { relation ->
        permissions.map { permission ->
            AccessTableConditions(
                command = command,
                permission = permission,
                relation = relation,
            )
        }
    }.any {
        accessTable[it] != null
    }

private data class AccessTableConditions(
    val command: CwpRatingCommand,
    val permission: CwpRatingUserPermissions,
    val relation: CwpRatingPrincipalRelations,
)

private val accessTable = mapOf(
    // Create
    AccessTableConditions(
        command = CwpRatingCommand.CREATE,
        permission = CwpRatingUserPermissions.CREATE_OWN,
        relation = CwpRatingPrincipalRelations.NEW,
    ) to true,

    // Read
    AccessTableConditions(
        command = CwpRatingCommand.READ,
        permission = CwpRatingUserPermissions.READ_OWN,
        relation = CwpRatingPrincipalRelations.OWN,
    ) to true,

    AccessTableConditions(
        command = CwpRatingCommand.READ,
        permission = CwpRatingUserPermissions.READ_PUBLIC,
        relation = CwpRatingPrincipalRelations.PUBLIC,
    ) to true,

    // Update
    AccessTableConditions(
        command = CwpRatingCommand.UPDATE,
        permission = CwpRatingUserPermissions.UPDATE_OWN,
        relation = CwpRatingPrincipalRelations.OWN,
    ) to true,

    // Delete
    AccessTableConditions(
        command = CwpRatingCommand.DELETE,
        permission = CwpRatingUserPermissions.DELETE_OWN,
        relation = CwpRatingPrincipalRelations.OWN,
    ) to true,
)