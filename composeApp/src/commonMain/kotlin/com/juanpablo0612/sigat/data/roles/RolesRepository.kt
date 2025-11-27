package com.juanpablo0612.sigat.data.roles

import com.juanpablo0612.sigat.data.exceptions.runSuspendCatchingApp
import com.juanpablo0612.sigat.data.roles.remote.RolesRemoteDataSource
import com.juanpablo0612.sigat.domain.model.Role

class RolesRepository(private val remoteDataSource: RolesRemoteDataSource) {
    suspend fun getRoles(): Result<List<Role>> {
        return runSuspendCatchingApp {
            val roleModels = remoteDataSource.getRoles()
            roleModels.map { it.toDomain() }
        }
    }

    suspend fun addRole(role: Role): Result<Unit> {
        return runSuspendCatchingApp {
            val roleModel = role.toModel()
            remoteDataSource.addRole(roleModel)
        }
    }
}
