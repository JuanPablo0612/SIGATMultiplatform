package com.juanpablo0612.sigat.domain.model

import com.juanpablo0612.sigat.data.obligations.model.ObligationModel

data class Obligation(
    val id: String = "",
    val number: Int = 0,
    val role: String = "",
    val name: String = ""
) {
    fun toModel() = ObligationModel(
        id = id,
        number = number,
        role = role,
        name = name
    )
}