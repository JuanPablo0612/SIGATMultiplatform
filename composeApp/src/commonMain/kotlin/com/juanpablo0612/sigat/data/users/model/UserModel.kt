package com.juanpablo0612.sigat.data.users.model

import com.juanpablo0612.sigat.data.roles.model.RoleModel
import com.juanpablo0612.sigat.domain.model.User
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val id: String,
    val uid: String,
    val firstName: String,
    val lastName: String,
    val lastDataUpdate: Timestamp,
    val role: RoleModel,
    val contractNumber: String
) {
    constructor() : this("", "", "", "", Timestamp.now(), RoleModel(), "")

    fun toDomain() = User(
        uid = uid,
        id = id,
        firstName = firstName,
        lastName = lastName,
        lastDataUpdate = lastDataUpdate,
        role = role.toDomain(),
        contractNumber = contractNumber
    )
}
