package com.juanpablo0612.sigat.domain.usecase.auth

class ValidatePasswordUseCase {
    operator fun invoke(password: String): Boolean {
        return password.length >= 8
    }
}