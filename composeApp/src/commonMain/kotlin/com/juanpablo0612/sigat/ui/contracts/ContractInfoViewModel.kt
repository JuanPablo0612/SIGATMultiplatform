package com.juanpablo0612.sigat.ui.contracts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanpablo0612.sigat.data.auth.AuthRepository
import com.juanpablo0612.sigat.data.contracts.ContractsRepository
import com.juanpablo0612.sigat.domain.model.Contract
import kotlinx.coroutines.launch

class ContractInfoViewModel(
    private val contractsRepository: ContractsRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    var uiState by mutableStateOf(ContractInfoUiState())
        private set

    init {
        loadContract()
    }

    private fun loadContract() {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true)
            val uid = authRepository.getUid()
            try {
                val contract = contractsRepository.getContract(uid)
                if (contract != null) {
                    uiState = uiState.copy(
                        city = contract.city,
                        supervisorName = contract.supervisorName,
                        supervisorPosition = contract.supervisorPosition,
                        supervisorDependency = contract.supervisorDependency,
                        number = contract.number,
                        year = contract.year,
                        contractorName = contract.contractorName,
                        contractorIdNumber = contract.contractorIdNumber,
                        contractorIdExpeditionPlace = contract.contractorIdExpeditionPlace,
                        contractObject = contract.contractObject,
                        value = contract.value,
                        paymentMethod = contract.paymentMethod,
                        elaborationDate = contract.elaborationDate,
                        endDate = contract.endDate,
                    )
                }
            } catch (e: Exception) {
                uiState = uiState.copy(exception = e)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    fun onCityChange(city: String) {
        uiState = uiState.copy(city = city, isValidCity = city.isNotBlank())
    }

    fun onSupervisorNameChange(name: String) {
        uiState = uiState.copy(
            supervisorName = name,
            isValidSupervisorName = name.isNotBlank(),
        )
    }

    fun onSupervisorPositionChange(position: String) {
        uiState = uiState.copy(
            supervisorPosition = position,
            isValidSupervisorPosition = position.isNotBlank(),
        )
    }

    fun onSupervisorDependencyChange(dependency: String) {
        uiState = uiState.copy(
            supervisorDependency = dependency,
            isValidSupervisorDependency = dependency.isNotBlank(),
        )
    }

    fun onContractNumberChange(number: String) {
        uiState = uiState.copy(number = number, isValidNumber = number.isNotBlank())
    }

    fun onContractYearChange(year: String) {
        uiState = uiState.copy(year = year, isValidYear = year.isNotBlank())
    }

    fun onContractorNameChange(name: String) {
        uiState = uiState.copy(
            contractorName = name,
            isValidContractorName = name.isNotBlank(),
        )
    }

    fun onContractorIdNumberChange(id: String) {
        uiState = uiState.copy(
            contractorIdNumber = id,
            isValidContractorIdNumber = id.isNotBlank(),
        )
    }

    fun onContractorIdExpeditionChange(place: String) {
        uiState = uiState.copy(
            contractorIdExpeditionPlace = place,
            isValidContractorIdExpedition = place.isNotBlank(),
        )
    }

    fun onContractObjectChange(obj: String) {
        uiState = uiState.copy(
            contractObject = obj,
            isValidContractObject = obj.isNotBlank(),
        )
    }

    fun onContractValueChange(value: String) {
        uiState = uiState.copy(value = value, isValidContractValue = value.isNotBlank())
    }

    fun onPaymentMethodChange(method: String) {
        uiState = uiState.copy(
            paymentMethod = method,
            isValidPaymentMethod = method.isNotBlank(),
        )
    }

    fun onElaborationDateChange(date: String) {
        uiState = uiState.copy(
            elaborationDate = date,
            isValidElaborationDate = date.isNotBlank(),
        )
    }

    fun onEndDateChange(date: String) {
        uiState = uiState.copy(endDate = date, isValidEndDate = date.isNotBlank())
    }

    private fun validateFields() {
        uiState = uiState.copy(
            isValidCity = uiState.city.isNotBlank(),
            isValidSupervisorName = uiState.supervisorName.isNotBlank(),
            isValidSupervisorPosition = uiState.supervisorPosition.isNotBlank(),
            isValidSupervisorDependency = uiState.supervisorDependency.isNotBlank(),
            isValidNumber = uiState.number.isNotBlank(),
            isValidYear = uiState.year.isNotBlank(),
            isValidContractorName = uiState.contractorName.isNotBlank(),
            isValidContractorIdNumber = uiState.contractorIdNumber.isNotBlank(),
            isValidContractorIdExpedition = uiState.contractorIdExpeditionPlace.isNotBlank(),
            isValidContractObject = uiState.contractObject.isNotBlank(),
            isValidContractValue = uiState.value.isNotBlank(),
            isValidPaymentMethod = uiState.paymentMethod.isNotBlank(),
            isValidElaborationDate = uiState.elaborationDate.isNotBlank(),
            isValidEndDate = uiState.endDate.isNotBlank(),
        )
    }

    private fun allFieldsValid(): Boolean {
        return uiState.isValidCity &&
            uiState.isValidSupervisorName &&
            uiState.isValidSupervisorPosition &&
            uiState.isValidSupervisorDependency &&
            uiState.isValidNumber &&
            uiState.isValidYear &&
            uiState.isValidContractorName &&
            uiState.isValidContractorIdNumber &&
            uiState.isValidContractorIdExpedition &&
            uiState.isValidContractObject &&
            uiState.isValidContractValue &&
            uiState.isValidPaymentMethod &&
            uiState.isValidElaborationDate &&
            uiState.isValidEndDate
    }

    fun onSave() {
        viewModelScope.launch {
            validateFields()
            if (!allFieldsValid()) return@launch

            uiState = uiState.copy(loading = true, exception = null)
            val uid = authRepository.getUid()
            val contract = Contract(
                userId = uid,
                city = uiState.city,
                elaborationDate = uiState.elaborationDate,
                supervisorName = uiState.supervisorName,
                supervisorPosition = uiState.supervisorPosition,
                supervisorDependency = uiState.supervisorDependency,
                number = uiState.number,
                year = uiState.year,
                contractorName = uiState.contractorName,
                contractorIdNumber = uiState.contractorIdNumber,
                contractorIdExpeditionPlace = uiState.contractorIdExpeditionPlace,
                contractObject = uiState.contractObject,
                value = uiState.value,
                paymentMethod = uiState.paymentMethod,
                endDate = uiState.endDate,
            )
            try {
                contractsRepository.saveContract(contract)
                uiState = uiState.copy(saved = true)
            } catch (e: Exception) {
                uiState = uiState.copy(exception = e)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }
}

data class ContractInfoUiState(
    val city: String = "",
    val isValidCity: Boolean = true,
    val supervisorName: String = "",
    val isValidSupervisorName: Boolean = true,
    val supervisorPosition: String = "",
    val isValidSupervisorPosition: Boolean = true,
    val supervisorDependency: String = "",
    val isValidSupervisorDependency: Boolean = true,
    val number: String = "",
    val isValidNumber: Boolean = true,
    val year: String = "",
    val isValidYear: Boolean = true,
    val contractorName: String = "",
    val isValidContractorName: Boolean = true,
    val contractorIdNumber: String = "",
    val isValidContractorIdNumber: Boolean = true,
    val contractorIdExpeditionPlace: String = "",
    val isValidContractorIdExpedition: Boolean = true,
    val contractObject: String = "",
    val isValidContractObject: Boolean = true,
    val value: String = "",
    val isValidContractValue: Boolean = true,
    val paymentMethod: String = "",
    val isValidPaymentMethod: Boolean = true,
    val elaborationDate: String = "",
    val isValidElaborationDate: Boolean = true,
    val endDate: String = "",
    val isValidEndDate: Boolean = true,
    val saved: Boolean = false,
    val loading: Boolean = false,
    val exception: Exception? = null,
)
