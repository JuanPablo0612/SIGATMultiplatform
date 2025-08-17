package com.juanpablo0612.sigat.data.training_programs.remote

import com.juanpablo0612.sigat.data.training_programs.model.TrainingProgramModel
import dev.gitlive.firebase.firestore.FieldValue.Companion.arrayRemove
import dev.gitlive.firebase.firestore.FieldValue.Companion.arrayUnion
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrainingProgramsRemoteDataSource(firestore: FirebaseFirestore) {
    private val trainingProgramsCollection = firestore.collection("training_programs")

    fun getTrainingProgramsByTeacherId(teacherId: String): Flow<List<TrainingProgramModel>> {
        return trainingProgramsCollection.where {
            "teacherUserId" equalTo teacherId
        }.snapshots.map { it.documents.map { document -> document.data() } }
    }

    suspend fun getTrainingProgram(id: String): TrainingProgramModel? {
        val snapshot = trainingProgramsCollection.document(id).get()
        return snapshot.data()
    }

    suspend fun createTrainingProgram(trainingProgram: TrainingProgramModel) {
        val id = trainingProgramsCollection.document.id
        trainingProgramsCollection.document(id).set(trainingProgram.copy(id = id))
    }

    suspend fun updateTrainingProgram(trainingProgram: TrainingProgramModel) {
        trainingProgramsCollection.document(trainingProgram.id).set(trainingProgram, merge = true)
    }

    suspend fun deleteTrainingProgram(id: String) {
        trainingProgramsCollection.document(id).delete()
    }

    suspend fun addStudentToTrainingProgram(trainingProgramId: String, studentUserId: String) {
        trainingProgramsCollection.document(trainingProgramId).update("students" to arrayUnion(studentUserId))
    }

    suspend fun removeStudentFromTrainingProgram(trainingProgramId: String, studentUserId: String) {
        trainingProgramsCollection.document(trainingProgramId).update("students" to arrayRemove(studentUserId))
    }
}