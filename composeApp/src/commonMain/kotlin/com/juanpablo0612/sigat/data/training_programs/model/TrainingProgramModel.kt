package com.juanpablo0612.sigat.data.training_programs.model

import kotlinx.serialization.Serializable

@Serializable
data class TrainingProgramModel(
    val id: String = "",
    val name: String = "",
    val code: Int = 0,
    val startDate: Long = 0L,
    val endDate: Long = 0L,
    val schedule: String = "",
    val teacherUserId: String = "",
    val students: List<String> = emptyList()
)
