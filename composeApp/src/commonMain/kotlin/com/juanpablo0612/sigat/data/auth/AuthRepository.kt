package com.juanpablo0612.sigat.data.auth

import com.juanpablo0612.sigat.data.auth.remote.AuthRemoteDataSource
import com.juanpablo0612.sigat.data.exceptions.handleExceptions

class AuthRepository(private val remoteDataSource: AuthRemoteDataSource) {
    suspend fun login(email: String, password: String) {
        handleExceptions {
            remoteDataSource.login(email, password)
        }
    }

    suspend fun register(email: String, password: String) {
        handleExceptions {
            remoteDataSource.register(email, password)
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