package com.juanpablo0612.sigat.domain.model

import com.juanpablo0612.sigat.data.contracts.model.ContractModel

/**
 * Domain representation of a contract used across the application layers.
 */
data class Contract(
    val userId: String = "",
    val city: String = "",
    val elaborationDate: String = "",
    val supervisorName: String = "",
    val supervisorPosition: String = "",
    val supervisorDependency: String = "",
    val number: String = "",
    val year: String = "",
    val contractorName: String = "",
    val contractorIdNumber: String = "",
    val contractorIdExpeditionPlace: String = "",
    val contractObject: String = "",
    val value: String = "",
    val paymentMethod: String = "",
    val endDate: String = "",
) {
    fun toModel() = ContractModel(
        userId = userId,
        city = city,
        elaborationDate = elaborationDate,
        supervisorName = supervisorName,
        supervisorPosition = supervisorPosition,
        supervisorDependency = supervisorDependency,
        number = number,
        year = year,
        contractorName = contractorName,
        contractorIdNumber = contractorIdNumber,
        contractorIdExpeditionPlace = contractorIdExpeditionPlace,
        contractObject = contractObject,
        value = value,
        paymentMethod = paymentMethod,
        endDate = endDate,
    )
}

