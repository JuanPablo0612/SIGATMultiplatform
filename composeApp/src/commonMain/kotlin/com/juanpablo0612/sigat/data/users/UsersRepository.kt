package com.juanpablo0612.sigat.data.users

import com.juanpablo0612.sigat.data.exceptions.mapAsResult
import com.juanpablo0612.sigat.data.exceptions.runSuspendCatchingApp
import com.juanpablo0612.sigat.data.users.remote.UsersRemoteDataSource
import com.juanpablo0612.sigat.domain.model.User

class UsersRepository(
    private val remoteDataSource: UsersRemoteDataSource,
) {
    fun getAllUsers() = remoteDataSource.getAllUsers()
        .mapAsResult { models -> models.map { userModel -> userModel.toDomain() } }

    suspend fun getUserByUid(uid: String): Result<User> {
        return runSuspendCatchingApp {
            remoteDataSource.getUserByUid(uid).toDomain()
        }
    }

    suspend fun getUsersByIds(ids: List<Long>): Result<List<User>> {
        return runSuspendCatchingApp {
            remoteDataSource.getUsersByIds(ids).map { it.toDomain() }
        }
    }

    suspend fun deleteUser(uid: String): Result<Boolean> {
        return runSuspendCatchingApp {
            remoteDataSource.deleteUser(uid)
            true
        }
    }

    suspend fun saveUser(user: User): Result<Boolean> {
        return runSuspendCatchingApp {
            val userModel = user.toModel()
            remoteDataSource.saveUser(userModel)
            true
        }
    }
}
