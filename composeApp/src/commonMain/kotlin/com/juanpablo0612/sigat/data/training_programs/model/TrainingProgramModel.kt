package com.juanpablo0612.sigat.data.training_programs.model

data class TrainingProgramModel(
    val id: String,
    val name: String,
    val code: Int,
    val startDate: Long,
    val endDate: Long,
    val schedule: String,
    val teacherUserId: String,
    val students: List<String>
)
