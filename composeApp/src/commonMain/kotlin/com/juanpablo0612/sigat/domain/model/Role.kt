package com.juanpablo0612.sigat.domain.model

import com.juanpablo0612.sigat.data.roles.model.RoleModel

data class Role(
    val id: String = "",
    val name: String = "",
    val description: String = ""
) {
    fun toModel() = RoleModel(
        id = id,
        name = name,
        description = description
    )
}