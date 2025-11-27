package com.juanpablo0612.sigat.data.contracts.remote

import com.juanpablo0612.sigat.data.contracts.model.ContractModel
import dev.gitlive.firebase.firestore.FirebaseFirestore

class ContractsRemoteDataSource(private val firestore: FirebaseFirestore) {
    private val contractsCollection = firestore.collection("contracts")

    suspend fun getContract(userId: String): ContractModel? {
        val document = contractsCollection.document(userId).get()
        return if (document.exists) {
            document.data(ContractModel.serializer())
        } else {
            null
        }
    }

    suspend fun saveContract(userId: String, contract: ContractModel) {
        contractsCollection.document(userId).set(contract)
    }
}
