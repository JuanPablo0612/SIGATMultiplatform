package com.juanpablo0612.sigat.domain.usecase.auth

class ValidateIdNumberUseCase {
    operator fun invoke(idNumber: String): Boolean {
        return idNumber.length >= 8 && idNumber.trim().all { it.isDigit() }
    }
} 