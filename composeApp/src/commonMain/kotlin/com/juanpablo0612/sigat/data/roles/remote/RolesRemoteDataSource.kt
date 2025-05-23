package com.juanpablo0612.sigat.data.roles.remote

import com.juanpablo0612.sigat.data.exceptions.handleExceptions
import com.juanpablo0612.sigat.data.roles.model.RoleModel
import dev.gitlive.firebase.firestore.FirebaseFirestore

class RolesRemoteDataSource(private val firestore: FirebaseFirestore) {
    private val rolesCollection = firestore.collection("roles")

    suspend fun getRoles(): List<RoleModel> {
        return handleExceptions {
            rolesCollection.get().documents.map {
                it.data(RoleModel.serializer())
            }
        }
    }

    suspend fun addRole(roleModel: RoleModel) {
        handleExceptions {
            val id = rolesCollection.document.id
            val newRoleModel = roleModel.copy(id = id)

            rolesCollection.document(id).set(newRoleModel)
        }
    }
}