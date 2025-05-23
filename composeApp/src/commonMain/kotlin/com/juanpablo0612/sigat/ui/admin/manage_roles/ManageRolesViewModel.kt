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
import kotlinx.coroutines.launch

class ManageRolesViewModel(
    private val usersRepository: UsersRepository,
    private val rolesRepository: RolesRepository
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
                val roles = rolesRepository.getRoles()
                usersRepository.getAllUsers().collect { users ->
                    uiState = uiState.copy(initialLoading = false, users = users, roles = roles)
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
                val updatedUsr = User(
                    uid = user.uid,
                    id = user.id,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    role = role,
                    contractNumber = user.contractNumber
                )

                usersRepository.updateUser(updatedUsr)
                uiState = uiState.copy(successChange = true, applyingChangesToUser = null)
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