package com.juanpablo0612.sigat.domain.usecase.auth

class ValidateIdIssuingLocationUseCase {
    operator fun invoke(idIssuingLocation: String): Boolean {
        return idIssuingLocation.trim().length >= 3
    }
} 