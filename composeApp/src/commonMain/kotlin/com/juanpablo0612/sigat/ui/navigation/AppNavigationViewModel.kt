package com.juanpablo0612.sigat.ui.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanpablo0612.sigat.data.auth.AuthRepository
import com.juanpablo0612.sigat.data.users.UsersRepository
import com.juanpablo0612.sigat.state_holders.UserState
import com.juanpablo0612.sigat.state_holders.UserStateHolder
import kotlinx.coroutines.launch

class AppNavigationViewModel(
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository,
    private val userStateHolder: UserStateHolder
) : ViewModel() {
    var uiState by mutableStateOf(AppNavigationUiState())
        private set

    init {
        loadCurrentUser()
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            val isSignedIn = authRepository.isSignedIn()
            if (!isSignedIn) {
                userStateHolder.clearUser()
                uiState = uiState.copy(
                    userState = userStateHolder.userState
                )
                return@launch
            }
            val userId = authRepository.getUid()
            val userResult = usersRepository.getUserByUid(userId)
            val user = userResult.getOrNull()
            if (user != null) {
                userStateHolder.updateUser(user)
            }

            uiState = uiState.copy(
                userState = userStateHolder.userState
            )
        }
    }
}

data class AppNavigationUiState(
    val userState: UserState? = null
)