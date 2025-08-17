package com.juanpablo0612.sigat.domain.usecase.auth

class ValidateLastNameUseCase {
    operator fun invoke(lastName: String): Boolean {
        return lastName.trim().length >= 2
    }
} 