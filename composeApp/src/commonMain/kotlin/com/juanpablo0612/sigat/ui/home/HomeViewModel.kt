package com.juanpablo0612.sigat.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanpablo0612.sigat.data.auth.AuthRepository
import com.juanpablo0612.sigat.data.users.UsersRepository
import com.juanpablo0612.sigat.domain.model.User
import kotlinx.coroutines.launch

class HomeViewModel(
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository,
) : ViewModel() {
    var uiState by mutableStateOf(HomeUIState())
        private set

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            try {
                val uid = authRepository.getUid()
                val user = usersRepository.getUserByUid(uid)
                uiState = uiState.copy(user = user, loading = false)
            } catch (e: Exception) {
                uiState = uiState.copy(loading = false, exception = e)
            }
        }
    }

    fun onLogout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}

data class HomeUIState(
    val user: User? = null,
    val loading: Boolean = true,
    val exception: Exception? = null
)