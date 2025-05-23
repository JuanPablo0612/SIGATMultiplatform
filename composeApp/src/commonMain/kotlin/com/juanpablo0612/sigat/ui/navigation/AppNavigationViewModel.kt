package com.juanpablo0612.sigat.ui.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanpablo0612.sigat.data.auth.AuthRepository
import kotlinx.coroutines.launch

class AppNavigationViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    var uiState by mutableStateOf(AppNavigationUiState())
        private set

    init {
        checkSignedIn()
    }

    private fun checkSignedIn() {
        viewModelScope.launch {
            val isSignedIn = authRepository.isSignedIn()
            val startDestination = if (!isSignedIn) Screen.Login else Screen.Home

            uiState = uiState.copy(
                startDestination = startDestination,
                accessSuccess = true
            )
        }
    }
}

data class AppNavigationUiState(
    val startDestination: Any? = null,
    val accessSuccess: Boolean = false,
    val emailVerified: Boolean = false,
    val dataUpdated: Boolean = false
)