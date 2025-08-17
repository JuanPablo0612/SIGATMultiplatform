package com.juanpablo0612.sigat.ui.auth.login

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanpablo0612.sigat.data.auth.AuthRepository
import com.juanpablo0612.sigat.domain.usecase.auth.ValidateEmailUseCase
import com.juanpablo0612.sigat.domain.usecase.auth.ValidatePasswordUseCase
import com.juanpablo0612.sigat.ui.utils.observeValidation
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : ViewModel() {
    var uiState by mutableStateOf(LoginUiState())
        private set

    val email = TextFieldState()
    val password = TextFieldState()

    init {
        email.observeValidation(
            viewModelScope = viewModelScope,
            validator = { validateEmailUseCase(it) },
            updateState = { uiState = uiState.copy(isValidEmail = it) }
        )
        
        password.observeValidation(
            viewModelScope = viewModelScope,
            validator = { validatePasswordUseCase(it) },
            updateState = { uiState = uiState.copy(isValidPassword = it) }
        )
    }

    fun onPasswordVisibilityChange() {
        uiState = uiState.copy(showPassword = !uiState.showPassword)
    }

    private fun validateFields() {
        val isValidEmail = validateEmailUseCase(email.text.toString())
        val isValidPassword = validatePasswordUseCase(password.text.toString())

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
                val emailStr = email.text.toString()
                val passwordStr = password.text.toString()

                val loginResult = authRepository.login(emailStr, passwordStr)
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
    val isValidEmail: Boolean = true,
    val isValidPassword: Boolean = true,
    val showPassword: Boolean = false,
    val success: Boolean = false,
    val loading: Boolean = false,
    val exception: Exception? = null
)