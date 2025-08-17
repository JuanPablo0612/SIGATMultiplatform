package com.juanpablo0612.sigat.ui.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
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
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.logout

@Composable
fun AppNavigation(
    viewModel: AppNavigationViewModel = koinViewModel(),
    windowSize: WindowSizeClass
) {
    val uiState = viewModel.uiState
    val navController = rememberNavController()

    if (uiState.userState != null) {
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
        val currentRoute = backStackEntry?.destination?.route
        val bottomRoutes = destinations.map { it.screen::class.qualifiedName }
        val showBottomBar = currentRoute in bottomRoutes

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

        val navHost: @Composable () -> Unit = {
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.imePadding(),
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
        }

        if (showBottomBar) {
            NavigationSuiteScaffold(
                navigationSuiteItems = {
                    destinations.forEach { destination ->
                        item(
                            icon = { Icon(imageVector = destination.icon, contentDescription = null) },
                            label = { Text(stringResource(destination.label)) },
                            selected = currentRoute == destination.screen::class.qualifiedName,
                            onClick = {
                                navController.navigate(destination.screen) {
                                    launchSingleTop = true
                                    popUpTo(startDestination) { saveState = true }
                                    restoreState = true
                                }
                            }
                        )
                    }
                    item(
                        icon = { Icon(imageVector = Icons.AutoMirrored.Filled.Logout, contentDescription = null) },
                        label = { Text(stringResource(Res.string.logout)) },
                        selected = false,
                        onClick = {
                            viewModel.logout()
                            navController.navigate(Screen.Login) {
                                popUpTo(startDestination) { inclusive = true }
                            }
                        }
                    )
                }
            ) { navHost() }
        } else {
            navHost()
        }
    } else {
        LoadingContent(modifier = Modifier.fillMaxSize())
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
