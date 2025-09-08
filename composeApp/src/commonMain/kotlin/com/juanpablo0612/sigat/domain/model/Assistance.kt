package com.juanpablo0612.sigat.domain.model

import com.juanpablo0612.sigat.data.assistance.model.AssistanceModel

/**
 * Domain model representing attendance for a student in a training program.
 */
data class Assistance(
    val id: String = "",
    val trainingProgramId: String = "",
    val studentId: String = "",
    val dateMillis: Long = 0L,
    val present: Boolean = false
) {
    fun toModel() = AssistanceModel(
        id = id,
        trainingProgramId = trainingProgramId,
        studentId = studentId,
        dateMillis = dateMillis,
        present = present
    )
}

fun AssistanceModel.toDomain() = Assistance(
    id = id,
    trainingProgramId = trainingProgramId,
    studentId = studentId,
    dateMillis = dateMillis,
    present = present
)

