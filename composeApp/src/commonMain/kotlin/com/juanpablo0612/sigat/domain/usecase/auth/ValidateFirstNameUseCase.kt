package com.juanpablo0612.sigat.domain.usecase.auth

class ValidateFirstNameUseCase {
    operator fun invoke(firstName: String): Boolean {
        return firstName.trim().length >= 2
    }
} 