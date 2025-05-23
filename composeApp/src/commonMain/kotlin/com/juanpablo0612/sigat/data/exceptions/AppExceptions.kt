package com.juanpablo0612.sigat.data.exceptions

import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidUserException
import dev.gitlive.firebase.auth.FirebaseAuthUserCollisionException

fun Exception.toAppException() = when (this) {
    is AppException -> this
    is FirebaseAuthUserCollisionException -> UserAlreadyExistsException()
    is FirebaseAuthInvalidUserException -> InvalidCredentialsException()
    is FirebaseAuthInvalidCredentialsException -> InvalidCredentialsException()
    else -> UnknownException()
}

inline fun <T> handleExceptions(action: () -> T): T {
    return try {
        action()
    } catch (e: Exception) {
        e.printStackTrace()
        throw e.toAppException()
    }
}

open class AppException : Exception()

class UserAlreadyExistsException : AppException()
class InvalidCredentialsException : AppException()
class UserIsNotSignedInException : AppException()
class DniIsNotRegisteredException : AppException()
class DniLinkedToAccountException() : AppException()
class UnknownException : AppException()
