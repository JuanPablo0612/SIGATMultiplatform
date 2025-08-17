package com.juanpablo0612.sigat.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
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
import com.juanpablo0612.sigat.ui.training_programs.list.TrainingProgramListScreen
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.logout

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    windowSize: WindowSizeClass,
    onNavigateToAddAction: () -> Unit,
    onNavigateToAddTrainingProgram: () -> Unit,
    onNavigateToTrainingProgramDetail: (String) -> Unit,
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
            item(
                icon = {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                },
                label = {
                    Text(stringResource(Res.string.logout))
                },
                selected = false,
                onClick = {
                    viewModel::onLogout
                    onLogout()
                }
            )
        }
    ) {
        if (currentDestination != null) {
            when (currentDestination) {
                HomeDestinations.ManageRoles -> {
                    ManageRolesScreen(windowSize = windowSize)
                }

                HomeDestinations.TrainingPrograms -> {
                    TrainingProgramListScreen(
                        windowSize = windowSize,
                        onAddProgramClick = onNavigateToAddTrainingProgram,
                        onProgramClick = onNavigateToTrainingProgramDetail
                    )
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