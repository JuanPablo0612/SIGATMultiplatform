package com.juanpablo0612.sigat.domain.usecase.auth

class ValidateConfirmPasswordUseCase {
    operator fun invoke(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }
} 