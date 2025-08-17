package com.juanpablo0612.sigat.ui.auth.register

import androidx.compose.foundation.text.input.TextFieldState
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
import com.juanpablo0612.sigat.ui.utils.observeValidation
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

    val firstName = TextFieldState()
    val lastName = TextFieldState()
    val idNumber = TextFieldState()
    val idIssuingLocation = TextFieldState()
    val email = TextFieldState()
    val password = TextFieldState()
    val confirmPassword = TextFieldState()

    init {
        firstName.observeValidation(
            viewModelScope = viewModelScope,
            validator = { validateFirstNameUseCase(it) },
            updateState = { uiState = uiState.copy(isValidFirstName = it) }
        )

        lastName.observeValidation(
            viewModelScope = viewModelScope,
            validator = { validateLastNameUseCase(it) },
            updateState = { uiState = uiState.copy(isValidLastName = it) }
        )

        idNumber.observeValidation(
            viewModelScope = viewModelScope,
            validator = { validateIdNumberUseCase(it) },
            updateState = { uiState = uiState.copy(isValidIdNumber = it) }
        )

        idIssuingLocation.observeValidation(
            viewModelScope = viewModelScope,
            validator = { validateIdIssuingLocationUseCase(it) },
            updateState = { uiState = uiState.copy(isValidIdIssuingLocation = it) }
        )

        email.observeValidation(
            viewModelScope = viewModelScope,
            validator = { validateEmailUseCase(it) },
            updateState = { uiState = uiState.copy(isValidEmail = it) }
        )

        password.observeValidation(
            viewModelScope = viewModelScope,
            validator = { validatePasswordUseCase(it) },
            updateState = {
                uiState = uiState.copy(isValidPassword = it)
                if (confirmPassword.text.isNotEmpty()) {
                    uiState = uiState.copy(
                        isValidConfirmPassword = validateConfirmPasswordUseCase(
                            password.text.toString(),
                            confirmPassword.text.toString()
                        )
                    )
                }
            }
        )

        confirmPassword.observeValidation(
            viewModelScope = viewModelScope,
            validator = { validateConfirmPasswordUseCase(password.text.toString(), it) },
            updateState = { uiState = uiState.copy(isValidConfirmPassword = it) }
        )
    }

    fun onPasswordVisibilityChange() {
        uiState = uiState.copy(showPassword = !uiState.showPassword)
    }

    private fun validateFields() {
        val isValidFirstName = validateFirstNameUseCase(firstName.text.toString())
        val isValidLastName = validateLastNameUseCase(lastName.text.toString())
        val isValidIdNumber = validateIdNumberUseCase(idNumber.text.toString())
        val isValidIdIssuingLocation =
            validateIdIssuingLocationUseCase(idIssuingLocation.text.toString())
        val isValidEmail = validateEmailUseCase(email.text.toString())
        val isValidPassword = validatePasswordUseCase(password.text.toString())
        val isValidConfirmPassword = validateConfirmPasswordUseCase(
            password.text.toString(),
            confirmPassword.text.toString()
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

            val emailStr = email.text.toString()
            val passwordStr = password.text.toString()
            val idNumberLong = idNumber.text.toString().toLong()
            val issuingLocationStr = idIssuingLocation.text.toString().trim()
            val firstNameStr = firstName.text.toString().trim()
            val lastNameStr = lastName.text.toString().trim()

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
    val isValidFirstName: Boolean = true,
    val isValidLastName: Boolean = true,
    val isValidIdNumber: Boolean = true,
    val isValidIdIssuingLocation: Boolean = true,
    val isValidEmail: Boolean = true,
    val isValidPassword: Boolean = true,
    val isValidConfirmPassword: Boolean = true,
    val showPassword: Boolean = false,
    val success: Boolean = false,
    val loading: Boolean = false,
    val exception: Exception? = null
)