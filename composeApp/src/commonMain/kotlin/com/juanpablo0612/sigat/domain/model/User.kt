package com.juanpablo0612.sigat.domain.model

import com.juanpablo0612.sigat.data.users.model.UserModel
import dev.gitlive.firebase.firestore.Timestamp

data class User(
    val uid: String = "",
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val lastDataUpdate: Timestamp = Timestamp.now(),
    val role: Role = Role(),
    val contractNumber: String = ""
) {
    fun toModel() = UserModel(
        uid = uid,
        id = id,
        firstName = firstName,
        lastName = lastName,
        lastDataUpdate = lastDataUpdate,
        role = role.toModel(),
        contractNumber = contractNumber
    )
}