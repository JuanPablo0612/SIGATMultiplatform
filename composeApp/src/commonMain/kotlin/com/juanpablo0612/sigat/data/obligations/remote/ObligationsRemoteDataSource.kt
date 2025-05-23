package com.juanpablo0612.sigat.data.obligations.remote

import com.juanpablo0612.sigat.data.exceptions.handleExceptions
import com.juanpablo0612.sigat.data.obligations.model.ObligationModel
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.map

class ObligationsRemoteDataSource(private val firestore: FirebaseFirestore) {
    private val obligationsCollection = firestore.collection("obligations")

    suspend fun getObligations(role: String): List<ObligationModel> {
        handleExceptions {
            val obligationDocuments = obligationsCollection.where {
                "role" equalTo role
            }

            return obligationDocuments.get().documents.map {
                it.data(ObligationModel.serializer())
            }
        }
    }
}