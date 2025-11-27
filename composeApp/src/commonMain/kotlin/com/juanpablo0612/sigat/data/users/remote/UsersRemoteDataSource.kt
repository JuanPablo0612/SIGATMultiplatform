package com.juanpablo0612.sigat.data.users.remote

import com.juanpablo0612.sigat.data.users.model.UserModel
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

    suspend fun getUsersByIds(ids: List<Long>): List<UserModel> {
        return usersCollection.where {
            "idNumber" inArray ids
        }.get().documents.map { it.data(UserModel.serializer()) }
    }

    suspend fun getUserByUid(uid: String): UserModel {
        return usersCollection.where {
            "uid" equalTo uid
        }.get().documents[0].data(UserModel.serializer())
    }

    suspend fun deleteUser(uid: String) {
        usersCollection.document(uid).delete()
    }

    suspend fun saveUser(userModel: UserModel) {
        val userDocument = usersCollection.document(userModel.uid)
        userDocument.set(userModel)
    }
}
