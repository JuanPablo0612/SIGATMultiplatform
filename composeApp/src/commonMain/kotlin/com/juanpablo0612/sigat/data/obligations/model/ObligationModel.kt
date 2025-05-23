package com.juanpablo0612.sigat.data.obligations.model

import com.juanpablo0612.sigat.domain.model.Obligation
import kotlinx.serialization.Serializable

@Serializable
data class ObligationModel(
    val id: String,
    val number: Int,
    val role: String,
    val name: String
) {
    constructor() : this("", 0, "", "")

    fun toDomain() = Obligation(
        id = id,
        number = number,
        role = role,
        name = name,
    )
}