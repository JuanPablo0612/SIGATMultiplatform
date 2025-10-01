package com.juanpablo0612.sigat.data.evaluations.model

data class EvaluationModel(
    val id: String,
    val trainingProgramId: String,
    val name: String,
    val percentage: Double,
    val grades: Map<Long, Double>
)
