package com.juanpablo0612.sigat.domain.usecase.auth

class ValidateEmailUseCase {
    operator fun invoke(email: String): Boolean {
        val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
        return emailRegex.matches(email)
    }
}