package com.juanpablo0612.sigat.data.contracts

import com.juanpablo0612.sigat.data.contracts.model.ContractModel
import com.juanpablo0612.sigat.domain.model.Contract

/**
 * Repository in charge of persisting contract data locally in memory.
 */
class ContractsRepository {
    private var contract: ContractModel? = null

    suspend fun saveContract(contract: Contract) {
        this.contract = contract.toModel()
    }

    suspend fun getContract(): Contract? = contract?.toDomain()
}

