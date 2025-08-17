package com.juanpablo0612.sigat.data.users.model

import com.juanpablo0612.sigat.data.roles.model.RoleModel
import com.juanpablo0612.sigat.domain.model.User
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val uid: String,
    val idNumber: Long,
    val idIssuingLocation: String,
    val firstName: String,
    val lastName: String,
    val lastDataUpdate: Timestamp,
    val role: RoleModel
) {
    fun toDomain() = User(
        uid = uid,
        idNumber = idNumber,
        idIssuingLocation = idIssuingLocation,
        firstName = firstName,
        lastName = lastName,
        lastDataUpdate = lastDataUpdate,
        role = role.toDomain()
    )
}
