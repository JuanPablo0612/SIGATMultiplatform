package com.juanpablo0612.sigat.ui.home

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.juanpablo0612.sigat.ui.actions.action_list.ActionListScreen
import com.juanpablo0612.sigat.ui.admin.manage_roles.ManageRolesScreen
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    windowSize: WindowSizeClass,
    onNavigateToAddAction: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState = viewModel.uiState
    var currentDestination: HomeDestinations? by remember { mutableStateOf(null) }
    val destinations = remember(uiState.user) {
        uiState.user?.let { getScreenListForRole(it.role) } ?: emptyList()
    }.also {
        if (it.isNotEmpty()) {
            if (currentDestination == null) {
                currentDestination = it[0]
            }
        }
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            destinations.forEach {
                item(
                    icon = {
                        Icon(imageVector = it.icon, contentDescription = null)
                    },
                    label = {
                        Text(stringResource(it.label))
                    },
                    selected = currentDestination == it,
                    onClick = {
                        currentDestination = it
                    }
                )
            }
        }
    ) {
        if (currentDestination != null) {
            when (currentDestination) {
                HomeDestinations.ManageRoles -> {
                    ManageRolesScreen(windowSize = windowSize)
                }

                HomeDestinations.Actions -> {
                    ActionListScreen(
                        windowSize = windowSize,
                        onAddActionClick = onNavigateToAddAction
                    )
                }

                HomeDestinations.Reports -> {

                }

                else -> {}
            }
        }
    }

}