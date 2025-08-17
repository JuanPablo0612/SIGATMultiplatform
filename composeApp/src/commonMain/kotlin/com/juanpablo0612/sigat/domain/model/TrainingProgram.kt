package com.juanpablo0612.sigat.domain.model

import com.juanpablo0612.sigat.data.training_programs.model.TrainingProgramModel

/**
 * Domain model representing a training program.
 */
data class TrainingProgram(
    val id: String = "",
    val name: String = "",
    val code: Int = 0,
    val startDate: Long = 0L,
    val endDate: Long = 0L,
    val schedule: String = "",
    val teacherUserId: String = "",
    val students: List<String> = emptyList()
) {
    fun toModel() = TrainingProgramModel(
        id = id,
        name = name,
        code = code,
        startDate = startDate,
        endDate = endDate,
        schedule = schedule,
        teacherUserId = teacherUserId,
        students = students
    )
}

fun TrainingProgramModel.toDomain() = TrainingProgram(
    id = id,
    name = name,
    code = code,
    startDate = startDate,
    endDate = endDate,
    schedule = schedule,
    teacherUserId = teacherUserId,
    students = students
)
