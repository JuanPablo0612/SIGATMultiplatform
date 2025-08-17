package com.juanpablo0612.sigat.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Report
import androidx.compose.ui.graphics.vector.ImageVector
import com.juanpablo0612.sigat.domain.model.Role
import com.juanpablo0612.sigat.domain.model.RoleType
import org.jetbrains.compose.resources.StringResource
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.actions_title
import sigat.composeapp.generated.resources.manage_roles_title
import sigat.composeapp.generated.resources.reports_title

enum class HomeDestinations(
    val label: StringResource,
    val icon: ImageVector
) {
    Actions(Res.string.actions_title, Icons.Default.History),
    Reports(Res.string.reports_title, Icons.Default.Report),
    ManageRoles(Res.string.manage_roles_title, Icons.Default.People)
}

fun getScreenListForRole(role: Role): List<HomeDestinations> {
    return when (role.type) {
        RoleType.ADMIN -> listOf(
            HomeDestinations.ManageRoles
        )

        RoleType.TEACHER -> {
            listOf(HomeDestinations.Actions, HomeDestinations.Reports)
        }

        else -> {
            listOf()
        }
    }
}