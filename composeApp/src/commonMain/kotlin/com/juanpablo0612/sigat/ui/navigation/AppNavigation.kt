package com.juanpablo0612.sigat.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.juanpablo0612.sigat.ui.actions.action_list.ActionListScreen
import com.juanpablo0612.sigat.ui.actions.add_action.AddActionScreen
import com.juanpablo0612.sigat.ui.admin.manage_roles.ManageRolesScreen
import com.juanpablo0612.sigat.ui.auth.login.LoginScreen
import com.juanpablo0612.sigat.ui.auth.register.RegisterScreen
import com.juanpablo0612.sigat.ui.components.LoadingContent
import com.juanpablo0612.sigat.ui.home.HomeDestinations
import com.juanpablo0612.sigat.ui.home.getScreenListForRole
import com.juanpablo0612.sigat.ui.reports.generate_report.GenerateReportScreen
import com.juanpablo0612.sigat.ui.training_programs.add.AddTrainingProgramScreen
import com.juanpablo0612.sigat.ui.training_programs.detail.TrainingProgramDetailScreen
import com.juanpablo0612.sigat.ui.training_programs.list.TrainingProgramListScreen
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation(
    viewModel: AppNavigationViewModel = koinViewModel(),
    windowSize: WindowSizeClass
) {
    val uiState = viewModel.uiState
    val navController = rememberNavController()

    Surface(color = MaterialTheme.colorScheme.background) {
        if (uiState.userState == null) {
            LoadingContent(modifier = Modifier.fillMaxSize())
        } else {
            val userState = uiState.userState
            val destinations = remember(userState.user) {
                userState.user?.let { getScreenListForRole(it.role) } ?: emptyList()
            }
            val startDestination = if (userState.isLoggedIn && destinations.isNotEmpty()) {
                destinations.first().screen
            } else {
                Screen.Login
            }

            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = backStackEntry?.destination
            val bottomRoutes = destinations.map { it.screen::class.qualifiedName }
            val showBottomBar = currentDestination?.route in bottomRoutes

            LaunchedEffect(userState.isLoggedIn) {
                if (userState.isLoggedIn) {
                    val first = destinations.firstOrNull()?.screen
                    val current = navController.currentBackStackEntry?.destination?.route
                    val target = first?.let { it::class.qualifiedName }
                    if (first != null && current != target) {
                        navController.navigate(first) {
                            popUpTo(Screen.Login) { inclusive = true }
                        }
                    }
                }
            }

            Column {
                NavHost(
                    navController = navController,
                    startDestination = startDestination,
                    modifier = Modifier.weight(1f).imePadding(),
                    enterTransition = {
                        slideInHorizontally { height -> height }
                    },
                    exitTransition = {
                        slideOutHorizontally { height -> -height }
                    },
                    popEnterTransition = {
                        slideInHorizontally { height -> -height }
                    },
                    popExitTransition = {
                        slideOutHorizontally { height -> height }
                    }
                ) {
                    addLoginScreen(navController, windowSize, viewModel::loadCurrentUser)
                    addRegisterScreen(navController, windowSize, viewModel::loadCurrentUser)
                    addManageRolesScreen(windowSize)
                    addTrainingProgramsScreen(navController, windowSize)
                    addActionsScreen(navController, windowSize)
                    addReportsScreen(windowSize)
                    addAddActionScreen(navController, windowSize)
                    addAddTrainingProgramScreen(navController, windowSize)
                    addTrainingProgramDetailScreen(navController, windowSize)
                }

                AnimatedVisibility(showBottomBar) {
                    BottomNavigationBar(
                        destinations = destinations,
                        currentDestination = currentDestination!!,
                        onNavigate = {
                            navController.navigate(it) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    destinations: List<HomeDestinations>,
    currentDestination: NavDestination,
    onNavigate: (Screen) -> Unit
) {
    NavigationBar {
        destinations.forEach { destination ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = stringResource(destination.label)
                    )
                },
                label = {
                    Text(
                        stringResource(destination.label),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                selected = currentDestination.hierarchy.any {
                    it.hasRoute(destination.screen::class)
                },
                onClick = { onNavigate(destination.screen) }
            )
        }
    }
}

fun NavGraphBuilder.addLoginScreen(
    navController: NavController,
    windowSize: WindowSizeClass,
    onLoginSuccess: () -> Unit
) {
    composable<Screen.Login> {
        LoginScreen(
            windowSize = windowSize,
            onNavigateToRegister = { navController.navigate(Screen.Register) },
            onNavigateToHome = { onLoginSuccess() }
        )
    }
}

fun NavGraphBuilder.addRegisterScreen(
    navController: NavController,
    windowSize: WindowSizeClass,
    onRegisterSuccess: () -> Unit
) {
    composable<Screen.Register> {
        RegisterScreen(
            windowSize = windowSize,
            onNavigateToHome = { onRegisterSuccess() },
            onNavigateBack = { navController.popBackStack() }
        )
    }
}

fun NavGraphBuilder.addManageRolesScreen(windowSize: WindowSizeClass) {
    composable<Screen.ManageRoles> {
        ManageRolesScreen(windowSize = windowSize)
    }
}

fun NavGraphBuilder.addTrainingProgramsScreen(
    navController: NavController,
    windowSize: WindowSizeClass
) {
    composable<Screen.TrainingPrograms> {
        TrainingProgramListScreen(
            windowSize = windowSize,
            onAddProgramClick = { navController.navigate(Screen.AddTrainingProgram) },
            onProgramClick = { id ->
                navController.navigate(Screen.TrainingProgramDetail(id))
            }
        )
    }
}

fun NavGraphBuilder.addActionsScreen(
    navController: NavController,
    windowSize: WindowSizeClass
) {
    composable<Screen.Actions> {
        ActionListScreen(
            windowSize = windowSize,
            onAddActionClick = { navController.navigate(Screen.AddAction) }
        )
    }
}

fun NavGraphBuilder.addReportsScreen(windowSize: WindowSizeClass) {
    composable<Screen.Reports> {
        GenerateReportScreen()
    }
}

fun NavGraphBuilder.addAddActionScreen(navController: NavController, windowSize: WindowSizeClass) {
    composable<Screen.AddAction> {
        AddActionScreen(
            windowSize = windowSize,
            onNavigateBack = {
                navController.navigateUp()
            }
        )
    }
}

fun NavGraphBuilder.addAddTrainingProgramScreen(
    navController: NavController,
    windowSize: WindowSizeClass
) {
    composable<Screen.AddTrainingProgram> {
        AddTrainingProgramScreen(
            windowSize = windowSize,
            onNavigateBack = { navController.navigateUp() }
        )
    }
}

fun NavGraphBuilder.addTrainingProgramDetailScreen(
    navController: NavController,
    windowSize: WindowSizeClass
) {
    composable<Screen.TrainingProgramDetail> {
        TrainingProgramDetailScreen(
            windowSize = windowSize,
            onNavigateBack = { navController.navigateUp() }
        )
    }
}
