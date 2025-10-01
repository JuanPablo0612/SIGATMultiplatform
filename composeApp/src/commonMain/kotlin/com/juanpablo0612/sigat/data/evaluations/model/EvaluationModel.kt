package com.juanpablo0612.sigat.data.evaluations.model

import kotlinx.serialization.Serializable

@Serializable
data class EvaluationModel(
    val id: String = "",
    val trainingProgramId: String = "",
    val name: String = "",
    val percentage: Double = 0.0,
    val grades: Map<Long, Double> = emptyMap()
)
