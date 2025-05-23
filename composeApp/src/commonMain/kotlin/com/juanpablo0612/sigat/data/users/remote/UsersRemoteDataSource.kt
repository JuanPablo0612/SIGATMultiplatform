package com.juanpablo0612.sigat.data.users.remote

import com.juanpablo0612.sigat.data.exceptions.handleExceptions
import com.juanpablo0612.sigat.data.users.model.UserModel
import com.juanpablo0612.sigat.domain.model.Role
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UsersRemoteDataSource(firestore: FirebaseFirestore) {
    private val usersCollection = firestore.collection("users")

    suspend fun getRole(dni: String): Role {
        return handleExceptions {
            val userDocument = usersCollection.document(dni).get()
            val userModel = userDocument.data(UserModel.serializer())
            userModel.role.toDomain()
        }
    }

    fun getAllUsers(): Flow<List<UserModel>> {
        return usersCollection.snapshots.map {
            it.documents.map { document ->
                document.data(UserModel.serializer())
            }
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

    suspend fun updateUser(userModel: UserModel) {
        handleExceptions {
            val userDocument = usersCollection.document(userModel.id)
            userDocument.set(userModel)
        }
    }
}