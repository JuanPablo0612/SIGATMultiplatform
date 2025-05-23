package com.juanpablo0612.sigat.ui.auth.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanpablo0612.sigat.data.auth.AuthRepository
import com.juanpablo0612.sigat.utils.checkEmail
import kotlinx.coroutines.launch

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {
    var uiState by mutableStateOf(RegisterUiState())
        private set

    fun onDniChange(dni: String) {
        uiState = uiState.copy(dni = dni)
    }

    fun onFirstNameChange(name: String) {
        uiState = uiState.copy(firstName = name)
    }

    fun onLastNameChange(name: String) {
        uiState = uiState.copy(lastName = name)
    }

    fun onEmailChange(email: String) {
        uiState = uiState.copy(email = email.trim())
    }

    fun onPasswordChange(password: String) {
        uiState = uiState.copy(password = password.trim())
    }

    fun onPasswordVisibilityChange(newVisibility: Boolean) {
        uiState = uiState.copy(isPasswordVisible = newVisibility)
    }

    private fun validateDni() {
        val validDni = uiState.dni.length >= 8
        uiState = uiState.copy(isValidDni = validDni)
    }

    private fun validateFirstName() {
        val validFirstName = uiState.firstName.isNotBlank()
        uiState = uiState.copy(isValidFirstName = validFirstName)
    }

    private fun validateLastName() {
        val validLastName = uiState.lastName.isNotBlank()
        uiState = uiState.copy(isValidLastName = validLastName)
    }

    private fun validateEmail() {
        uiState = uiState.copy(isValidEmail = checkEmail(uiState.email))
    }

    private fun validatePassword() {
        val validPassword = uiState.password.length >= 8
        uiState = uiState.copy(isValidPassword = validPassword)
    }

    private fun validateFields() {
        validateDni()
        validateFirstName()
        validateLastName()
        validateEmail()
        validatePassword()
    }

    fun onRegister() {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true)
            validateFields()

            try {
                authRepository.register(uiState.email, uiState.password)
                uiState = uiState.copy(success = true)
            } catch (e: Exception) {
                uiState = uiState.copy(exception = e)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }
}

data class RegisterUiState(
    val dni: String = "",
    val isValidDni: Boolean = true,
    val firstName: String = "",
    val isValidFirstName: Boolean = true,
    val lastName: String = "",
    val isValidLastName: Boolean = true,
    val email: String = "",
    val isValidEmail: Boolean = true,
    val password: String = "",
    val isValidPassword: Boolean = true,
    val confirmPassword: String = "",
    val isValidConfirmPassword: Boolean = true,
    val isPasswordVisible: Boolean = false,
    val success: Boolean = false,
    val loading: Boolean = false,
    val exception: Exception? = null
)