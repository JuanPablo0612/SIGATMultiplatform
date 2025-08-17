package com.juanpablo0612.sigat.data.training_programs

import com.juanpablo0612.sigat.data.training_programs.remote.TrainingProgramsRemoteDataSource
import com.juanpablo0612.sigat.domain.model.TrainingProgram
import com.juanpablo0612.sigat.domain.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrainingProgramsRepository(private val remoteDataSource: TrainingProgramsRemoteDataSource) {
    fun getTrainingProgramsByTeacherId(teacherId: String): Flow<List<TrainingProgram>> {
        return remoteDataSource.getTrainingProgramsByTeacherId(teacherId).map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun getTrainingProgram(id: String): TrainingProgram? {
        return remoteDataSource.getTrainingProgram(id)?.toDomain()
    }

    suspend fun createTrainingProgram(trainingProgram: TrainingProgram) {
        remoteDataSource.createTrainingProgram(trainingProgram.toModel())
    }

    suspend fun updateTrainingProgram(trainingProgram: TrainingProgram) {
        remoteDataSource.updateTrainingProgram(trainingProgram.toModel())
    }

    suspend fun deleteTrainingProgram(id: String) {
        remoteDataSource.deleteTrainingProgram(id)
    }

    suspend fun addStudentToTrainingProgram(trainingProgramId: String, studentUserId: String) {
        remoteDataSource.addStudentToTrainingProgram(trainingProgramId, studentUserId)
    }

    suspend fun removeStudentFromTrainingProgram(trainingProgramId: String, studentUserId: String) {
        remoteDataSource.removeStudentFromTrainingProgram(trainingProgramId, studentUserId)
    }
}
