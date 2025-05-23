package com.juanpablo0612.sigat.data.roles.model

import com.juanpablo0612.sigat.domain.model.Role
import kotlinx.serialization.Serializable

@Serializable
data class RoleModel(
    val id: String,
    val name: String,
    val description: String
) {
    constructor() : this("", "", "")

    fun toDomain() = Role(
        id = id,
        name = name,
        description = description
    )
}