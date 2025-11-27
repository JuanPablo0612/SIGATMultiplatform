package com.juanpablo0612.sigat.ui.admin.manage_roles

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanpablo0612.sigat.data.roles.RolesRepository
import com.juanpablo0612.sigat.data.users.UsersRepository
import com.juanpablo0612.sigat.domain.model.Role
import com.juanpablo0612.sigat.domain.model.User
import com.juanpablo0612.sigat.state_holders.UserStateHolder
import kotlinx.coroutines.launch

class ManageRolesViewModel(
    private val usersRepository: UsersRepository,
    private val rolesRepository: RolesRepository,
    private val userStateHolder: UserStateHolder,
) : ViewModel() {
    var uiState by mutableStateOf(ManageRolesUiState())
        private set

    init {
        getRequiredData()
    }

    private fun getRequiredData() {
        viewModelScope.launch {
            uiState = uiState.copy(initialLoading = true)

            try {
                val currentUserId = userStateHolder.userState.user?.uid ?: return@launch
                val rolesResult = rolesRepository.getRoles()
                var roles: List<Role> = emptyList()
                rolesResult
                    .onSuccess { fetchedRoles -> roles = fetchedRoles }
                    .onFailure { exception ->
                        uiState = uiState.copy(initialLoading = false, exception = exception as Exception)
                        return@launch
                    }

                usersRepository.getAllUsers().collect { usersResult ->
                    usersResult.fold(
                        onSuccess = { users ->
                            uiState = uiState.copy(
                                users = users.filterNot { it.uid == currentUserId },
                                roles = roles,
                                initialLoading = false,
                                exception = null
                            )
                        },
                        onFailure = { failure ->
                            uiState = uiState.copy(initialLoading = false, exception = failure as Exception)
                        }
                    )
                }
            } catch (e: Exception) {
                uiState = uiState.copy(initialLoading = false, exception = e)
            }
        }
    }

    fun updateUserRole(user: User, role: Role) {
        viewModelScope.launch {
            uiState = uiState.copy(applyingChangesToUser = user)

            try {
                val updateUser = user.copy(role = role)

                val saveResult = usersRepository.saveUser(updateUser)
                saveResult
                    .onSuccess {
                        uiState = uiState.copy(successChange = true, applyingChangesToUser = null)
                    }
                    .onFailure { exception ->
                        uiState = uiState.copy(
                            applyingChangesToUser = null,
                            successChange = false,
                            exception = exception as Exception
                        )
                    }
            } catch (e: Exception) {
                uiState =
                    uiState.copy(applyingChangesToUser = null, successChange = false, exception = e)
            }
        }
    }
}

data class ManageRolesUiState(
    val initialLoading: Boolean = false,
    val applyingChangesToUser: User? = null,
    val successChange: Boolean? = null,
    val users: List<User> = emptyList(),
    val roles: List<Role> = emptyList(),
    val exception: Exception? = null
)
