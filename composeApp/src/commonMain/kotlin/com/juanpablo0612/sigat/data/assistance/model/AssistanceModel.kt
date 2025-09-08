package com.juanpablo0612.sigat.data.assistance.model

import kotlinx.serialization.Serializable

@Serializable
data class AssistanceModel(
    val id: String = "",
    val trainingProgramId: String = "",
    val studentId: String = "",
    val dateMillis: Long = 0L,
    val present: Boolean = false
)

