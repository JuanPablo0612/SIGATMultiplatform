package com.juanpablo0612.sigat.ui.auth.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanpablo0612.sigat.data.auth.AuthRepository
import com.juanpablo0612.sigat.data.users.UsersRepository
import com.juanpablo0612.sigat.domain.model.User
import com.juanpablo0612.sigat.domain.usecase.auth.ValidateConfirmPasswordUseCase
import com.juanpablo0612.sigat.domain.usecase.auth.ValidateEmailUseCase
import com.juanpablo0612.sigat.domain.usecase.auth.ValidateFirstNameUseCase
import com.juanpablo0612.sigat.domain.usecase.auth.ValidateIdIssuingLocationUseCase
import com.juanpablo0612.sigat.domain.usecase.auth.ValidateIdNumberUseCase
import com.juanpablo0612.sigat.domain.usecase.auth.ValidateLastNameUseCase
import com.juanpablo0612.sigat.domain.usecase.auth.ValidatePasswordUseCase
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validateFirstNameUseCase: ValidateFirstNameUseCase,
    private val validateLastNameUseCase: ValidateLastNameUseCase,
    private val validateIdNumberUseCase: ValidateIdNumberUseCase,
    private val validateIdIssuingLocationUseCase: ValidateIdIssuingLocationUseCase,
    private val validateConfirmPasswordUseCase: ValidateConfirmPasswordUseCase
) : ViewModel() {
    var uiState by mutableStateOf(RegisterUiState())
        private set

    fun onFirstNameChange(firstName: String) {
        uiState = uiState.copy(
            firstName = firstName,
            isValidFirstName = validateFirstNameUseCase(firstName.trim())
        )
    }

    fun onLastNameChange(lastName: String) {
        uiState = uiState.copy(
            lastName = lastName,
            isValidLastName = validateLastNameUseCase(lastName.trim())
        )
    }

    fun onIdNumberChange(idNumber: String) {
        uiState = uiState.copy(
            idNumber = idNumber.trim(),
            isValidIdNumber = validateIdNumberUseCase(idNumber.trim())
        )
    }

    fun onIdIssuingLocationChange(idIssuingLocation: String) {
        uiState = uiState.copy(
            idIssuingLocation = idIssuingLocation,
            isValidIdIssuingLocation = validateIdIssuingLocationUseCase(idIssuingLocation.trim())
        )
    }

    fun onEmailChange(email: String) {
        uiState =
            uiState.copy(email = email.trim(), isValidEmail = validateEmailUseCase(email.trim()))
    }

    fun onPasswordChange(password: String) {
        uiState = uiState.copy(
            password = password.trim(),
            isValidPassword = validatePasswordUseCase(password.trim())
        )
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        uiState = uiState.copy(
            confirmPassword = confirmPassword,
            isValidConfirmPassword = validateConfirmPasswordUseCase(
                uiState.password,
                confirmPassword.trim()
            )
        )
    }

    fun onPasswordVisibilityChange() {
        uiState = uiState.copy(showPassword = !uiState.showPassword)
    }

    private fun validateFields() {
        val isValidFirstName = validateFirstNameUseCase(uiState.firstName)
        val isValidLastName = validateLastNameUseCase(uiState.lastName)
        val isValidIdNumber = validateIdNumberUseCase(uiState.idNumber)
        val isValidIdIssuingLocation =
            validateIdIssuingLocationUseCase(uiState.idIssuingLocation)
        val isValidEmail = validateEmailUseCase(uiState.email)
        val isValidPassword = validatePasswordUseCase(uiState.password)
        val isValidConfirmPassword = validateConfirmPasswordUseCase(
            uiState.password,
            uiState.confirmPassword
        )

        uiState = uiState.copy(
            isValidFirstName = isValidFirstName,
            isValidLastName = isValidLastName,
            isValidIdNumber = isValidIdNumber,
            isValidIdIssuingLocation = isValidIdIssuingLocation,
            isValidEmail = isValidEmail,
            isValidPassword = isValidPassword,
            isValidConfirmPassword = isValidConfirmPassword
        )
    }

    private fun allFieldsValid(): Boolean {
        return uiState.isValidFirstName &&
                uiState.isValidLastName &&
                uiState.isValidIdNumber &&
                uiState.isValidIdIssuingLocation &&
                uiState.isValidEmail &&
                uiState.isValidPassword &&
                uiState.isValidConfirmPassword
    }

    fun onRegisterClick() {
        viewModelScope.launch {
            validateFields()

            if (!allFieldsValid()) return@launch

            uiState = uiState.copy(loading = true, exception = null)

            val emailStr = uiState.email
            val passwordStr = uiState.password
            val idNumberLong = uiState.idNumber.toLong()
            val issuingLocationStr = uiState.idIssuingLocation
            val firstNameStr = uiState.firstName
            val lastNameStr = uiState.lastName

            val registerResult = authRepository.register(emailStr, passwordStr)
            registerResult
                .onSuccess { uid ->
                    val user = User(
                        uid = uid,
                        idNumber = idNumberLong,
                        idIssuingLocation = issuingLocationStr,
                        firstName = firstNameStr,
                        lastName = lastNameStr
                    )

                    val saveResult = usersRepository.saveUser(user)
                    saveResult
                        .onSuccess {
                            uiState = uiState.copy(success = true)
                        }
                        .onFailure { e ->
                            uiState = uiState.copy(exception = e as Exception)
                        }
                }
                .onFailure { e ->
                    uiState = uiState.copy(exception = e as Exception)
                }

            uiState = uiState.copy(loading = false)
        }
    }
}

data class RegisterUiState(
    val firstName: String = "",
    val isValidFirstName: Boolean = true,
    val lastName: String = "",
    val isValidLastName: Boolean = true,
    val idNumber: String = "",
    val isValidIdNumber: Boolean = true,
    val idIssuingLocation: String = "",
    val isValidIdIssuingLocation: Boolean = true,
    val email: String = "",
    val isValidEmail: Boolean = true,
    val password: String = "",
    val isValidPassword: Boolean = true,
    val confirmPassword: String = "",
    val isValidConfirmPassword: Boolean = true,
    val showPassword: Boolean = false,
    val success: Boolean = false,
    val loading: Boolean = false,
    val exception: Exception? = null
)