package com.juanpablo0612.sigat.data.users.remote

import com.juanpablo0612.sigat.data.exceptions.handleExceptions
import com.juanpablo0612.sigat.data.users.model.UserModel
import com.juanpablo0612.sigat.domain.model.Role
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UsersRemoteDataSource(firestore: FirebaseFirestore) {
    private val usersCollection = firestore.collection("users")

    fun getAllUsers(): Flow<List<UserModel>> {
        return usersCollection.snapshots.map {
            it.documents.map { document ->
                document.data(UserModel.serializer())
            }
        }
    }

    suspend fun getUsersByIds(ids: List<String>): List<UserModel> {
        return handleExceptions {
            val users = usersCollection.where {
                "idNumber" inArray ids
            }.get()

            users.documents.map { it.data(UserModel.serializer()) }
        }
    }

    suspend fun getUserByUid(uid: String): UserModel {
        return handleExceptions {
            usersCollection.where {
                "uid" equalTo uid
            }.get().documents[0].data(UserModel.serializer())
        }
    }

    suspend fun deleteUser(uid: String) {
        handleExceptions {
            usersCollection.document(uid).delete()
        }
    }

    suspend fun saveUser(userModel: UserModel) {
        val userDocument = usersCollection.document(userModel.uid)
        userDocument.set(userModel)
    }
}