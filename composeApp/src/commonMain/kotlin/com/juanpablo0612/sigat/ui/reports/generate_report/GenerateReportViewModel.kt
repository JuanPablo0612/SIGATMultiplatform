package com.juanpablo0612.sigat.ui.reports.generate_report

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanpablo0612.sigat.data.actions.ActionsRepository
import com.juanpablo0612.sigat.data.auth.AuthRepository
import com.juanpablo0612.sigat.data.reports.ReportsRepository
import com.juanpablo0612.sigat.data.users.UsersRepository
import com.juanpablo0612.sigat.data.contracts.ContractsRepository
import com.juanpablo0612.sigat.domain.model.Contract
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class GenerateReportViewModel(
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository,
    private val actionsRepository: ActionsRepository,
    private val reportsRepository: ReportsRepository,
    private val contractsRepository: ContractsRepository,
) : ViewModel() {
    var uiState by mutableStateOf((GenerateReportUiState()))
        private set

    init {
        viewModelScope.launch {
            contractsRepository.getContract()?.let { contract ->
                uiState = uiState.copy(contract = contract)
            }
        }
    }

    fun onTemplateFileSelected(file: PlatformFile) {
        uiState = uiState.copy(templateFile = file)
    }

    fun onOutputFileSelected(file: PlatformFile) {
        uiState = uiState.copy(outputFile = file)
    }

    private fun updateContract(update: Contract.() -> Contract) {
        uiState = uiState.copy(contract = uiState.contract.update())
    }

    fun onCityChange(city: String) = updateContract { copy(city = city) }
    fun onSupervisorNameChange(name: String) = updateContract { copy(supervisorName = name) }
    fun onSupervisorPositionChange(position: String) = updateContract { copy(supervisorPosition = position) }
    fun onSupervisorDependencyChange(dependency: String) = updateContract { copy(supervisorDependency = dependency) }
    fun onContractNumberChange(number: String) = updateContract { copy(number = number) }
    fun onContractYearChange(year: String) = updateContract { copy(year = year) }
    fun onContractorNameChange(name: String) = updateContract { copy(contractorName = name) }
    fun onContractorIdNumberChange(id: String) = updateContract { copy(contractorIdNumber = id) }
    fun onContractorIdExpeditionChange(place: String) = updateContract { copy(contractorIdExpeditionPlace = place) }
    fun onContractObjectChange(obj: String) = updateContract { copy(contractObject = obj) }
    fun onContractValueChange(value: String) = updateContract { copy(value = value) }
    fun onPaymentMethodChange(method: String) = updateContract { copy(paymentMethod = method) }
    fun onEndDateChange(date: String) = updateContract { copy(endDate = date) }
    fun onElaborationDateChange(date: String) = updateContract { copy(elaborationDate = date) }

    fun onStartTimestampChange(timestamp: Long) {
        uiState = uiState.copy(startTimestamp = timestamp)
    }

    fun onEndTimestampChange(timestamp: Long) {
        uiState = uiState.copy(endTimestamp = timestamp)
    }

    fun onGenerateReportClick() {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true)

            try {
                val uid = authRepository.getUid()
                val userResult = usersRepository.getUserByUid(uid = uid)

                userResult.fold(
                    onSuccess = { user ->
                        val actions = actionsRepository.getActions(user.uid).first()
                            .filter { it.timestamp in uiState.startTimestamp..uiState.endTimestamp }
                        contractsRepository.saveContract(uiState.contract)
                        reportsRepository.generateReport(
                            template = uiState.templateFile!!,
                            output = uiState.outputFile!!,
                            user = user,
                            contract = uiState.contract,
                            startTimestamp = uiState.startTimestamp,
                            endTimestamp = uiState.endTimestamp,
                            actions = actions
                        )
                        uiState = uiState.copy(success = true)
                    },
                    onFailure = { uiState = uiState.copy(exception = it as Exception) }
                )
            } catch (e: Exception) {
                uiState = uiState.copy(exception = e)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }
}

data class GenerateReportUiState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val exception: Exception? = null,
    val templateFile: PlatformFile? = null,
    val outputFile: PlatformFile? = null,
    val contract: Contract = Contract(),
    val startTimestamp: Long = 0,
    val endTimestamp: Long = 0,
)