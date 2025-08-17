package com.juanpablo0612.sigat.data.actions.model

import com.juanpablo0612.sigat.domain.model.Action
import kotlinx.serialization.Serializable

@Serializable
data class ActionModel(
    val id: String,
    val creatorUid: String,
    val obligationNumber: Int,
    val obligationName: String,
    val description: String,
    val images: List<String>,
    val timestamp: Long
) {
    constructor() : this("", "", 0, "", "", emptyList(), 0)

    fun toDomain() = Action(
        id = id,
        creatorUid = creatorUid,
        obligationNumber = obligationNumber,
        obligationName = obligationName,
        description = description,
        images = images,
        timestamp = timestamp
    )
}