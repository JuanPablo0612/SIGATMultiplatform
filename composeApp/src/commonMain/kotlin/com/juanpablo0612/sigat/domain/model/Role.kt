package com.juanpablo0612.sigat.domain.model

import com.juanpablo0612.sigat.data.roles.model.RoleModel

enum class RoleType {
    STUDENT,
    TEACHER,
    ADMIN
}

data class Role(
    val id: String = "aprendiz",
    val name: String = "Aprendiz",
    val description: String = "",
    val type: RoleType = RoleType.STUDENT
) {
    fun toModel() = RoleModel(
        id = id,
        name = name,
        description = description
    )
}