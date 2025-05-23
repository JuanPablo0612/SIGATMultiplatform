package com.juanpablo0612.sigat.ui.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanpablo0612.sigat.data.auth.AuthRepository
import com.juanpablo0612.sigat.utils.checkEmail
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(newEmail: String) {
        uiState = uiState.copy(email = newEmail.trim())
    }

    fun onPasswordChange(newPassword: String) {
        uiState = uiState.copy(password = newPassword.trim())
    }

    fun onPasswordVisibilityChange(newVisibility: Boolean) {
        uiState = uiState.copy(isPasswordVisible = newVisibility)
    }

    private fun validateEmail() {
        uiState = uiState.copy(isValidEmail = checkEmail(uiState.email))
    }

    private fun validatePassword() {
        val validPassword = uiState.password.length >= 8
        uiState = uiState.copy(isValidPassword = validPassword)
    }

    private fun validateFields() {
        validateEmail()
        validatePassword()
    }

    fun onLogin() {
        viewModelScope.launch {
            validateFields()

            uiState = uiState.copy(loading = true)

            try {
                authRepository.login(uiState.email, uiState.password)
                uiState = uiState.copy(success = true)
            } catch (e: Exception) {
                uiState = uiState.copy(exception = e)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }
}

data class LoginUiState(
    val email: String = "",
    val isValidEmail: Boolean = true,
    val password: String = "",
    val isValidPassword: Boolean = true,
    val isPasswordVisible: Boolean = false,
    val success: Boolean = false,
    val loading: Boolean = false,
    val exception: Exception? = null
)