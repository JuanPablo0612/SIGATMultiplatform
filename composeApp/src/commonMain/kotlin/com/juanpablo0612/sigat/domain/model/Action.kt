package com.juanpablo0612.sigat.domain.model

import com.juanpablo0612.sigat.data.actions.model.ActionModel

data class Action(
    val id: String = "",
    val uid: String = "",
    val obligationNumber: Int = 0,
    val obligationName: String = "",
    val description: String = "",
    val images: List<String> = emptyList(),
    val timestamp: Long = 0
) {
    fun toModel() = ActionModel(
        id = id,
        dni = uid,
        obligationNumber = obligationNumber,
        obligationName = obligationName,
        description = description,
        images = images,
        timestamp = timestamp
    )
}