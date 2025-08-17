package com.juanpablo0612.sigat.data.auth

import com.juanpablo0612.sigat.data.auth.remote.AuthRemoteDataSource
import com.juanpablo0612.sigat.data.exceptions.toAppException
import dev.gitlive.firebase.auth.FirebaseAuthException

class AuthRepository(private val remoteDataSource: AuthRemoteDataSource) {
    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val uid = remoteDataSource.login(email, password)
            Result.success(uid)
        } catch (e: FirebaseAuthException) {
            Result.failure(e.toAppException())
        }
    }

    suspend fun register(email: String, password: String): Result<String> {
        return try {
            val uid = remoteDataSource.register(email, password)
            Result.success(uid)
        } catch (e: FirebaseAuthException) {
            Result.failure(e.toAppException())
        }
    }

    suspend fun logout() {
        remoteDataSource.logout()
    }

    fun isSignedIn(): Boolean {
        return remoteDataSource.isSignedIn()
    }

    fun getUid(): String {
        return remoteDataSource.getUid()
    }
}