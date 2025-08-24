package com.juanpablo0612.sigat.data.contracts

import com.juanpablo0612.sigat.data.contracts.remote.ContractsRemoteDataSource
import com.juanpablo0612.sigat.domain.model.Contract

/**
 * Repository in charge of persisting contract data remotely in Firebase.
 */
class ContractsRepository(
    private val remoteDataSource: ContractsRemoteDataSource,
) {
    suspend fun saveContract(contract: Contract) {
        remoteDataSource.saveContract(contract.userId, contract.toModel())
    }

    suspend fun getContract(userId: String): Contract? {
        return remoteDataSource.getContract(userId)?.toDomain()
    }
}

