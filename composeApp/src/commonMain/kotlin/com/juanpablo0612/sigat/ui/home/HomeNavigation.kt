package com.juanpablo0612.sigat.ui.home

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.juanpablo0612.sigat.domain.model.Role
import com.juanpablo0612.sigat.ui.admin.manage_roles.ManageRolesScreen
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.reports_title

sealed class Features(val nameId: StringResource) {
    data object Reports : Features(Res.string.reports_title)
}

fun NavGraphBuilder.addManageRolesScreen(navController: NavController, windowSize: WindowSizeClass) {
    composable<ManageRoles> {
        ManageRolesScreen(windowSize = windowSize)
    }
}

fun NavGraphBuilder.addActionScreen(navController: NavController) {
    composable<Actions> { }
}

fun getScreenListForRole(role: Role): List<Any> {
    return when (role.id) {
        "admin" -> listOf(
            ManageRoles
        )

        "facilitador" -> {
            listOf(Actions)
        }

        else -> {
            listOf()
        }
    }
}

@Serializable
object ManageRoles

@Serializable
object Actions

@Serializable
object Reports