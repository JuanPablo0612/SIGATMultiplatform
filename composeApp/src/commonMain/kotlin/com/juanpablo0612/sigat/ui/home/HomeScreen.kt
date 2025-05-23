package com.juanpablo0612.sigat.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.juanpablo0612.sigat.domain.model.User
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinInject(),
    windowSize: WindowSizeClass,
    onNavigateToAddAction: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState = viewModel.uiState
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                if (uiState.user != null) {
                    UserCard(
                        user = uiState.user,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                HomeTopBar(
                    featureName = uiState.user?.firstName ?: "",
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = onNavigateToAddAction) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
        ) { innerPadding ->
            if (uiState.user != null) {
                val screens = remember(uiState.user) { getScreenListForRole(uiState.user.role) }

                NavHost(
                    startDestination = screens[0],
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    screens.forEach {
                        when (it) {
                            ManageRoles -> {
                                addManageRolesScreen(navController,  windowSize)
                            }

                            Actions -> {
                                addActionScreen(navController)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserCard(user: User, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .padding(end = 8.dp)
            )

            Column {
                Text(
                    text = user.firstName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = user.role.name,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}