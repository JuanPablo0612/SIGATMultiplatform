package com.juanpablo0612.sigat.ui.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.juanpablo0612.sigat.ui.actions.add_action.AddActionScreen
import com.juanpablo0612.sigat.ui.auth.login.LoginScreen
import com.juanpablo0612.sigat.ui.auth.register.RegisterScreen
import com.juanpablo0612.sigat.ui.components.LoadingContent
import com.juanpablo0612.sigat.ui.home.HomeScreen
import com.juanpablo0612.sigat.ui.reports.generate_report.GenerateReportScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation(
    viewModel: AppNavigationViewModel = koinViewModel(),
    windowSize: WindowSizeClass
) {
    val uiState = viewModel.uiState
    val navController = rememberNavController()

    if (uiState.startDestination != null) {
        NavHost(
            navController = navController,
            startDestination = uiState.startDestination,
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
            addLoginScreen(navController = navController, windowSize = windowSize)
            addRegisterScreen(navController = navController, windowSize = windowSize)
            addHomeScreen(navController = navController, windowSize = windowSize)
            addAddActionScreen(navController = navController, windowSize = windowSize)
            addGenerateReportScreen(navController = navController, windowSize = windowSize)
        }
    } else {
        LoadingContent(modifier = Modifier.fillMaxSize())
    }
}

fun NavGraphBuilder.addLoginScreen(navController: NavController, windowSize: WindowSizeClass) {
    composable<Screen.Login> {
        LoginScreen(
            windowSize = windowSize,
            onNavigateToRegister = { navController.navigate(Screen.Register) },
            onNavigateToHome = { navController.navigate(Screen.Home) }
        )
    }
}

fun NavGraphBuilder.addRegisterScreen(navController: NavController, windowSize: WindowSizeClass) {
    composable<Screen.Register> {
        RegisterScreen(
            windowSize = windowSize,
            onNavigateToHome = { navController.navigate(Screen.Home) },
            onNavigateBack = { navController.popBackStack() }
        )
    }
}

fun NavGraphBuilder.addHomeScreen(navController: NavController, windowSize: WindowSizeClass) {
    composable<Screen.Home> {
        HomeScreen(
            windowSize = windowSize,
            onNavigateToAddAction = {
                navController.navigate(Screen.AddAction)
            },
            onLogout = {
                navController.navigate(Screen.Login) {
                    popUpTo(Screen.Home) {
                        inclusive = true
                    }
                }
            }
        )
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

fun NavGraphBuilder.addGenerateReportScreen(
    navController: NavController,
    windowSize: WindowSizeClass
) {
    composable<Screen.GenerateReport> {
        GenerateReportScreen()
    }
}