package com.juanpablo0612.sigat.data.assistance

import com.juanpablo0612.sigat.data.assistance.remote.AssistanceRemoteDataSource
import com.juanpablo0612.sigat.data.exceptions.mapAsResult
import com.juanpablo0612.sigat.data.exceptions.runSuspendCatchingApp
import com.juanpablo0612.sigat.domain.model.Assistance
import com.juanpablo0612.sigat.domain.model.toDomain

class AssistanceRepository(private val remoteDataSource: AssistanceRemoteDataSource) {
    fun getAssistanceForProgramAndDate(programId: String, dateMillis: Long) =
        remoteDataSource.getAssistanceForProgramAndDate(programId, dateMillis)
            .mapAsResult { list -> list.map { it.toDomain() } }

    suspend fun setAttendance(
        programId: String,
        studentIdNumber: Long,
        dateMillis: Long,
        present: Boolean,
    ): Result<Unit> {
        return runSuspendCatchingApp {
            remoteDataSource.setAttendance(programId, studentIdNumber, dateMillis, present)
        }
    }
}
