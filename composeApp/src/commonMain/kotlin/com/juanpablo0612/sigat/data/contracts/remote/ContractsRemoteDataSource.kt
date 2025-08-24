package com.juanpablo0612.sigat.data.contracts.remote

import com.juanpablo0612.sigat.data.contracts.model.ContractModel
import com.juanpablo0612.sigat.data.exceptions.handleExceptions
import dev.gitlive.firebase.firestore.FirebaseFirestore

class ContractsRemoteDataSource(private val firestore: FirebaseFirestore) {
    private val contractsCollection = firestore.collection("contracts")

    suspend fun getContract(userId: String): ContractModel? {
        return handleExceptions {
            val document = contractsCollection.document(userId).get()
            if (document.exists) {
                document.data(ContractModel.serializer())
            } else {
                null
            }
        }
    }

    suspend fun saveContract(userId: String, contract: ContractModel) {
        handleExceptions {
            contractsCollection.document(userId).set(contract)
        }
    }
}
