package com.juanpablo0612.sigat.data.exceptions

import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidUserException
import dev.gitlive.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

fun Throwable.toAppException(): AppException = when (this) {
    is AppException -> this
    is CancellationException -> throw this
    is FirebaseAuthUserCollisionException -> UserAlreadyExistsException()
    is FirebaseAuthInvalidUserException -> InvalidCredentialsException()
    is FirebaseAuthInvalidCredentialsException -> InvalidCredentialsException()
    else -> UnknownException()
}

suspend inline fun <T> runSuspendCatchingApp(crossinline block: suspend () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (throwable: Throwable) {
        if (throwable is CancellationException) throw throwable
        Result.failure(throwable.toAppException())
    }
}

inline fun <T> runBlockingCatchingApp(block: () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (throwable: Throwable) {
        if (throwable is CancellationException) throw throwable
        Result.failure(throwable.toAppException())
    }
}

fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return map { value -> Result.success(value) }
        .catch { throwable ->
            if (throwable is CancellationException) throw throwable
            emit(Result.failure(throwable.toAppException()))
        }
}

fun <T, R> Flow<T>.mapAsResult(transform: (T) -> R): Flow<Result<R>> {
    return map { value -> Result.success(transform(value)) }
        .catch { throwable ->
            if (throwable is CancellationException) throw throwable
            emit(Result.failure(throwable.toAppException()))
        }
}

open class AppException : Exception()

class UserAlreadyExistsException : AppException()
class InvalidCredentialsException : AppException()
class UserIsNotSignedInException : AppException()
class DniIsNotRegisteredException : AppException()
class DniLinkedToAccountException() : AppException()
class UnknownException : AppException()
