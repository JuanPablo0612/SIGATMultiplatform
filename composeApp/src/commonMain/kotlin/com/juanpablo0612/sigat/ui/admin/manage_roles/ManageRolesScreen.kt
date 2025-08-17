package com.juanpablo0612.sigat.ui.admin.manage_roles

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.juanpablo0612.sigat.domain.model.Role
import com.juanpablo0612.sigat.domain.model.User
import com.juanpablo0612.sigat.ui.components.ErrorCard
import com.juanpablo0612.sigat.ui.components.LoadingContent
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.unknown_error_message

@Composable
fun ManageRolesScreen(
    viewModel: ManageRolesViewModel = koinViewModel(),
    windowSize: WindowSizeClass
) {
    val uiState = viewModel.uiState
    val cols = remember(windowSize.widthSizeClass) {
        when (windowSize.widthSizeClass) {
            WindowWidthSizeClass.Compact -> GridCells.Fixed(1)
            WindowWidthSizeClass.Medium -> GridCells.Fixed(2)
            WindowWidthSizeClass.Expanded -> GridCells.Adaptive(300.dp)
            else -> GridCells.Fixed(1)
        }
    }

    Scaffold(
        topBar = {
            ManageRolesTopBar()
        }
    ) {
        Column {
            if (uiState.initialLoading) {
                LoadingContent(modifier = Modifier.fillMaxSize())
            } else if (uiState.exception != null) {
                ErrorCard(message = stringResource(Res.string.unknown_error_message))
            } else {
                ManageRolesContent(
                    users = uiState.users,
                    roles = uiState.roles,
                    cols = cols,
                    onUserRoleChange = viewModel::updateUserRole
                )
            }
        }
    }
}

@Composable
private fun ManageRolesContent(
    users: List<User>,
    roles: List<Role>,
    cols: GridCells,
    applyingChangesToUser: User? = null,
    onUserRoleChange: (User, Role) -> Unit
) {
    LazyVerticalGrid(
        columns = cols,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(users) {
            UserRoleCard(
                user = it,
                roles = roles,
                modifier = Modifier.fillMaxWidth(),
                applyingChanges = it == applyingChangesToUser,
                onUserRoleChange = { role ->
                    onUserRoleChange(it, role)
                }
            )
        }
    }
}

@Composable
private fun UserRoleCard(
    user: User,
    roles: List<Role>,
    modifier: Modifier = Modifier,
    applyingChanges: Boolean = false,
    onUserRoleChange: (Role) -> Unit,
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = user.idNumber.toString(),
                    style = MaterialTheme.typography.bodySmall
                )

                AnimatedVisibility(applyingChanges) {
                    CircularProgressIndicator()
                }
            }
            Text(
                text = user.firstName,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = user.role.name,
                style = MaterialTheme.typography.bodyMedium
            )

            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                roles.forEach {
                    UserRoleChip(
                        role = it,
                        selected = it.id == user.role.id,
                        onClick = {
                            onUserRoleChange(it)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun UserRoleChip(
    modifier: Modifier = Modifier,
    role: Role,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        modifier = modifier,
        selected = selected,
        onClick = onClick,
        label = {
            Text(text = role.name)
        },
    )
}