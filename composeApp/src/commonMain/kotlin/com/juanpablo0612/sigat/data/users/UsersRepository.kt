package com.juanpablo0612.sigat.data.users

import com.juanpablo0612.sigat.data.users.remote.UsersRemoteDataSource
import com.juanpablo0612.sigat.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UsersRepository(
    private val remoteDataSource: UsersRemoteDataSource,
) {
    suspend fun getRole(dni: String) = remoteDataSource.getRole(dni)

    fun getAllUsers(): Flow<List<User>> {
        val userModels = remoteDataSource.getAllUsers()
        return userModels.map { it.map { model -> model.toDomain() } }
    }

    suspend fun getUserByUid(uid: String): User {
        val userModel = remoteDataSource.getUserByUid(uid)
        return userModel.toDomain()
    }

    suspend fun deleteUser(uid: String) {
        remoteDataSource.deleteUser(uid)
    }

    suspend fun updateUser(user: User) {
        val userModel = user.toModel()
        remoteDataSource.updateUser(userModel)
    }
}