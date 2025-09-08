package com.juanpablo0612.sigat.data.assistance

import com.juanpablo0612.sigat.data.assistance.remote.AssistanceRemoteDataSource
import com.juanpablo0612.sigat.domain.model.Assistance
import com.juanpablo0612.sigat.domain.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AssistanceRepository(private val remoteDataSource: AssistanceRemoteDataSource) {
    fun getAssistanceForProgramAndDate(programId: String, dateMillis: Long): Flow<List<Assistance>> {
        return remoteDataSource.getAssistanceForProgramAndDate(programId, dateMillis).map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun setAttendance(programId: String, studentId: String, dateMillis: Long, present: Boolean) {
        remoteDataSource.setAttendance(programId, studentId, dateMillis, present)
    }
}

