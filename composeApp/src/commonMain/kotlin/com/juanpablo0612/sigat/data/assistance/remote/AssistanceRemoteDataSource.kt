package com.juanpablo0612.sigat.data.assistance.remote

import com.juanpablo0612.sigat.data.assistance.model.AssistanceModel
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AssistanceRemoteDataSource(firestore: FirebaseFirestore) {
    private val assistanceCollection = firestore.collection("assistance")

    fun getAssistanceForProgramAndDate(programId: String, dateMillis: Long): Flow<List<AssistanceModel>> {
        return assistanceCollection.where {
            "trainingProgramId" equalTo programId
            "dateMillis" equalTo dateMillis
        }.snapshots.map { it.documents.map { doc -> doc.data() } }
    }

    suspend fun setAttendance(programId: String, studentId: String, dateMillis: Long, present: Boolean) {
        val id = "${'$'}programId_${'$'}dateMillis_${'$'}studentId"
        assistanceCollection.document(id).set(
            AssistanceModel(
                id = id,
                trainingProgramId = programId,
                studentId = studentId,
                dateMillis = dateMillis,
                present = present
            )
        )
    }
}

