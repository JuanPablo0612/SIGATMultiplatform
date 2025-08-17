package com.juanpablo0612.sigat.data.users

import com.juanpablo0612.sigat.data.exceptions.toAppException
import com.juanpablo0612.sigat.data.users.remote.UsersRemoteDataSource
import com.juanpablo0612.sigat.domain.model.User
import dev.gitlive.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UsersRepository(
    private val remoteDataSource: UsersRemoteDataSource,
) {
    fun getAllUsers(): Flow<Result<List<User>>> {
        return remoteDataSource.getAllUsers()
            .map {
                Result.success(it.map { userModel -> userModel.toDomain() })
            }.catch {
                emit(Result.failure((it as Exception).toAppException()))
            }
    }

    suspend fun getUserByUid(uid: String): Result<User> {
        return try {
            val userModel = remoteDataSource.getUserByUid(uid)
            Result.success(userModel.toDomain())
        } catch (e: FirebaseFirestoreException) {
            Result.failure(e.toAppException())
        }
    }

    suspend fun getUsersByIds(ids: List<String>): Result<List<User>> {
        return try {
            val userModels = remoteDataSource.getUsersByIds(ids)
            Result.success(userModels.map { it.toDomain() })
        } catch (e: FirebaseFirestoreException) {
            Result.failure(e.toAppException())
        }
    }

    suspend fun deleteUser(uid: String): Result<Boolean> {
        return try {
            remoteDataSource.deleteUser(uid)
            Result.success(true)
        } catch (e: FirebaseFirestoreException) {
            Result.failure(e.toAppException())
        }
    }

    suspend fun saveUser(user: User): Result<Boolean> {
        return try {
            val userModel = user.toModel()
            remoteDataSource.saveUser(userModel)
            Result.success(true)
        } catch (e: FirebaseFirestoreException) {
            Result.failure(e.toAppException())
        }
    }
}