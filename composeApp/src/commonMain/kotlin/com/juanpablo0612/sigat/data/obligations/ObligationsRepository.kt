package com.juanpablo0612.sigat.data.obligations

import com.juanpablo0612.sigat.data.exceptions.runSuspendCatchingApp
import com.juanpablo0612.sigat.data.obligations.remote.ObligationsRemoteDataSource
import com.juanpablo0612.sigat.domain.model.Obligation

class ObligationsRepository(private val dataSource: ObligationsRemoteDataSource) {
    suspend fun getObligations(role: String): Result<List<Obligation>> {
        return runSuspendCatchingApp {
            dataSource.getObligations(role).map { model -> model.toDomain() }
        }
    }
}
