package com.juanpablo0612.sigat.domain.model

import com.juanpablo0612.sigat.data.evaluations.model.EvaluationModel

/**
 * Domain model representing an evaluation associated with a training program.
 */
data class Evaluation(
    val id: String = "",
    val trainingProgramId: String = "",
    val name: String = "",
    val percentage: Double = 0.0,
    val grades: Map<Long, Double> = emptyMap()
) {
    fun toModel() = EvaluationModel(
        id = id,
        trainingProgramId = trainingProgramId,
        name = name,
        percentage = percentage,
        grades = grades
    )
}

fun EvaluationModel.toDomain() = Evaluation(
    id = id,
    trainingProgramId = trainingProgramId,
    name = name,
    percentage = percentage,
    grades = grades
)
