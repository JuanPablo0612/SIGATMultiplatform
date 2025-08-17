package com.juanpablo0612.sigat.ui.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanpablo0612.sigat.data.auth.AuthRepository
import com.juanpablo0612.sigat.domain.usecase.auth.ValidateEmailUseCase
import com.juanpablo0612.sigat.domain.usecase.auth.ValidatePasswordUseCase
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : ViewModel() {
    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(email: String) {
        uiState = uiState.copy(email = email.trim(), isValidEmail = validateEmailUseCase(email.trim()))
    }

    fun onPasswordChange(password: String) {
        uiState = uiState.copy(password = password.trim(), isValidPassword = validatePasswordUseCase(password.trim()))
    }

    fun onPasswordVisibilityChange() {
        uiState = uiState.copy(showPassword = !uiState.showPassword)
    }

    private fun validateFields() {
        val isValidEmail = validateEmailUseCase(uiState.email)
        val isValidPassword = validatePasswordUseCase(uiState.password)

        uiState = uiState.copy(
            isValidEmail = isValidEmail,
            isValidPassword = isValidPassword
        )
    }

    private fun allFieldsValid(): Boolean {
        return uiState.isValidEmail && uiState.isValidPassword
    }

    fun onLoginClick() {
        viewModelScope.launch {
            validateFields()

            if (allFieldsValid()) {
                uiState = uiState.copy(loading = true)

                val loginResult = authRepository.login(uiState.email, uiState.password)
                loginResult
                    .onSuccess {
                        uiState = uiState.copy(success = true)
                    }
                    .onFailure { e ->
                        uiState = uiState.copy(exception = e as Exception)
                    }

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
    val showPassword: Boolean = false,
    val success: Boolean = false,
    val loading: Boolean = false,
    val exception: Exception? = null
)