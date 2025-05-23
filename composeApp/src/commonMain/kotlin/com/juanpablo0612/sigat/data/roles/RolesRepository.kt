package com.juanpablo0612.sigat.data.roles

import com.juanpablo0612.sigat.data.roles.remote.RolesRemoteDataSource
import com.juanpablo0612.sigat.domain.model.Role

class RolesRepository(private val remoteDataSource: RolesRemoteDataSource) {
    suspend fun getRoles(): List<Role> {
        val roleModels = remoteDataSource.getRoles()
        return roleModels.map { it.toDomain() }
    }

    suspend fun addRole(role: Role) {
        val roleModel = role.toModel()
        remoteDataSource.addRole(roleModel)
    }
}