package com.juanpablo0612.sigat.data.training_programs

import com.juanpablo0612.sigat.data.exceptions.mapAsResult
import com.juanpablo0612.sigat.data.exceptions.runSuspendCatchingApp
import com.juanpablo0612.sigat.data.training_programs.remote.TrainingProgramsRemoteDataSource
import com.juanpablo0612.sigat.domain.model.TrainingProgram
import com.juanpablo0612.sigat.domain.model.toDomain

class TrainingProgramsRepository(private val remoteDataSource: TrainingProgramsRemoteDataSource) {
    fun getTrainingProgramsByTeacherId(teacherId: String) =
        remoteDataSource.getTrainingProgramsByTeacherId(teacherId)
            .mapAsResult { list -> list.map { it.toDomain() } }

    suspend fun getTrainingProgram(id: String): Result<TrainingProgram?> {
        return runSuspendCatchingApp {
            remoteDataSource.getTrainingProgram(id)?.toDomain()
        }
    }

    suspend fun createTrainingProgram(trainingProgram: TrainingProgram): Result<Unit> {
        return runSuspendCatchingApp {
            remoteDataSource.createTrainingProgram(trainingProgram.toModel())
        }
    }

    suspend fun updateTrainingProgram(trainingProgram: TrainingProgram): Result<Unit> {
        return runSuspendCatchingApp {
            remoteDataSource.updateTrainingProgram(trainingProgram.toModel())
        }
    }

    suspend fun deleteTrainingProgram(id: String): Result<Unit> {
        return runSuspendCatchingApp {
            remoteDataSource.deleteTrainingProgram(id)
        }
    }

    suspend fun addStudentToTrainingProgram(trainingProgramId: String, studentUserId: String): Result<Unit> {
        return runSuspendCatchingApp {
            remoteDataSource.addStudentToTrainingProgram(trainingProgramId, studentUserId)
        }
    }

    suspend fun removeStudentFromTrainingProgram(trainingProgramId: String, studentIdNumber: Long): Result<Unit> {
        return runSuspendCatchingApp {
            remoteDataSource.removeStudentFromTrainingProgram(trainingProgramId, studentIdNumber)
        }
    }
}
