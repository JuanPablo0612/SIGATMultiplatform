package com.juanpablo0612.sigat.data.training_programs

import com.juanpablo0612.sigat.data.training_programs.model.TrainingProgramModel
import com.juanpablo0612.sigat.data.training_programs.remote.TrainingProgramsRemoteDataSource
import kotlinx.coroutines.flow.Flow

class TrainingProgramsRepository(private val remoteDataSource: TrainingProgramsRemoteDataSource) {
    fun getTrainingProgramsByTeacherId(teacherId: String): Flow<List<TrainingProgramModel>> {
        return remoteDataSource.getTrainingProgramsByTeacherId(teacherId)
    }

    suspend fun getTrainingProgram(id: String): TrainingProgramModel? {
        return remoteDataSource.getTrainingProgram(id)
    }

    suspend fun createTrainingProgram(trainingProgram: TrainingProgramModel) {
        remoteDataSource.createTrainingProgram(trainingProgram)
    }

    suspend fun updateTrainingProgram(trainingProgram: TrainingProgramModel) {
        remoteDataSource.updateTrainingProgram(trainingProgram)
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