package com.juanpablo0612.sigat.data.evaluations

import com.juanpablo0612.sigat.data.evaluations.remote.EvaluationsRemoteDataSource
import com.juanpablo0612.sigat.domain.model.Evaluation
import com.juanpablo0612.sigat.domain.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EvaluationsRepository(private val remoteDataSource: EvaluationsRemoteDataSource) {
    fun getEvaluationsByTrainingProgram(trainingProgramId: String): Flow<List<Evaluation>> {
        return remoteDataSource.getEvaluationsByTrainingProgram(trainingProgramId).map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun addEvaluation(evaluation: Evaluation) {
        remoteDataSource.addEvaluation(evaluation.toModel())
    }

    suspend fun updateEvaluation(evaluation: Evaluation) {
        remoteDataSource.updateEvaluation(evaluation.toModel())
    }

    suspend fun setGrades(evaluationId: String, grades: Map<Long, Double>) {
        remoteDataSource.setGrades(evaluationId, grades)
    }

    suspend fun deleteEvaluation(evaluationId: String) {
        remoteDataSource.deleteEvaluation(evaluationId)
    }
}
