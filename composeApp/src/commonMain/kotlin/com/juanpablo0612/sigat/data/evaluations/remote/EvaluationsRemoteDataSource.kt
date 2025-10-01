package com.juanpablo0612.sigat.data.evaluations.remote

import com.juanpablo0612.sigat.data.evaluations.model.EvaluationModel
import dev.gitlive.firebase.firestore.FirebaseFirestore

class EvaluationsRemoteDataSource(private val firestore: FirebaseFirestore) {
    private val evaluationsCollection = firestore.collection("evaluations")

    suspend fun addEvaluation(evaluation: EvaluationModel) {
        val document = evaluationsCollection.document.id
        evaluationsCollection.document(document).set(evaluation.copy(id = document))
    }

    suspend fun setGrades(evaluationId: String, grades: Map<Long, Double>) {
        evaluationsCollection.document(evaluationId).update("grades" to grades)
    }
}