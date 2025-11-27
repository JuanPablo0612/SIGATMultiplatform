package com.juanpablo0612.sigat.data.evaluations

import com.juanpablo0612.sigat.data.evaluations.remote.EvaluationsRemoteDataSource
import com.juanpablo0612.sigat.data.exceptions.mapAsResult
import com.juanpablo0612.sigat.data.exceptions.runSuspendCatchingApp
import com.juanpablo0612.sigat.domain.model.Evaluation
import com.juanpablo0612.sigat.domain.model.toDomain

class EvaluationsRepository(private val remoteDataSource: EvaluationsRemoteDataSource) {
    fun getEvaluationsByTrainingProgram(trainingProgramId: String) =
        remoteDataSource.getEvaluationsByTrainingProgram(trainingProgramId)
            .mapAsResult { list -> list.map { it.toDomain() } }

    suspend fun addEvaluation(evaluation: Evaluation): Result<Unit> {
        return runSuspendCatchingApp {
            remoteDataSource.addEvaluation(evaluation.toModel())
        }
    }

    suspend fun updateEvaluation(evaluation: Evaluation): Result<Unit> {
        return runSuspendCatchingApp {
            remoteDataSource.updateEvaluation(evaluation.toModel())
        }
    }

    suspend fun setGrades(evaluationId: String, grades: Map<Long, Double>): Result<Unit> {
        return runSuspendCatchingApp {
            remoteDataSource.setGrades(evaluationId, grades)
        }
    }

    suspend fun deleteEvaluation(evaluationId: String): Result<Unit> {
        return runSuspendCatchingApp {
            remoteDataSource.deleteEvaluation(evaluationId)
        }
    }
}
