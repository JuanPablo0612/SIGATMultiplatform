package com.juanpablo0612.sigat.data.evaluations.remote

import com.juanpablo0612.sigat.data.evaluations.model.EvaluationModel
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EvaluationsRemoteDataSource(private val firestore: FirebaseFirestore) {
    private val evaluationsCollection = firestore.collection("evaluations")

    fun getEvaluationsByTrainingProgram(trainingProgramId: String): Flow<List<EvaluationModel>> {
        return evaluationsCollection.where {
            "trainingProgramId" equalTo trainingProgramId
        }.snapshots.map { snapshot -> snapshot.documents.map { it.data() } }
    }

    suspend fun addEvaluation(evaluation: EvaluationModel) {
        val document = evaluationsCollection.document.id
        evaluationsCollection.document(document).set(evaluation.copy(id = document))
    }

    suspend fun updateEvaluation(evaluation: EvaluationModel) {
        evaluationsCollection.document(evaluation.id).set(evaluation, merge = true)
    }

    suspend fun setGrades(evaluationId: String, grades: Map<Long, Double>) {
        evaluationsCollection.document(evaluationId).update("grades" to grades)
    }

    suspend fun deleteEvaluation(evaluationId: String) {
        evaluationsCollection.document(evaluationId).delete()
    }
}
