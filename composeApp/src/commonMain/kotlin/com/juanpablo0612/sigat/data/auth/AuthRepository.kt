package com.juanpablo0612.sigat.data.auth

import com.juanpablo0612.sigat.data.auth.remote.AuthRemoteDataSource
import com.juanpablo0612.sigat.data.exceptions.runBlockingCatchingApp
import com.juanpablo0612.sigat.data.exceptions.runSuspendCatchingApp

class AuthRepository(private val remoteDataSource: AuthRemoteDataSource) {
    suspend fun login(email: String, password: String): Result<String> {
        return runSuspendCatchingApp {
            remoteDataSource.login(email, password)
        }
    }

    suspend fun register(email: String, password: String): Result<String> {
        return runSuspendCatchingApp {
            remoteDataSource.register(email, password)
        }
    }

    suspend fun logout(): Result<Unit> {
        return runSuspendCatchingApp {
            remoteDataSource.logout()
        }
    }

    fun isSignedIn(): Boolean {
        return remoteDataSource.isSignedIn()
    }

    fun getUid(): Result<String> {
        return runBlockingCatchingApp { remoteDataSource.getUid() }
    }
}
