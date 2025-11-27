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
    var uiState by mutableStateOf(GenerateReportUiState())
        private set

    init {
        loadContract()
    }

    fun loadContract() {
        viewModelScope.launch {
            val uid = authRepository.getUid().getOrElse { throwable ->
                uiState = uiState.copy(exception = throwable.asException())
                return@launch
            }
            val contractResult = contractsRepository.getContract(uid)
            contractResult
                .onSuccess { contract -> uiState = uiState.copy(contract = contract) }
                .onFailure { exception -> uiState = uiState.copy(exception = exception.asException()) }
        }
    }

    fun onTemplateFileSelected(file: PlatformFile) {
        uiState = uiState.copy(templateFile = file)
    }

    fun onOutputFileSelected(file: PlatformFile) {
        uiState = uiState.copy(outputFile = file)
    }

    fun onStartTimestampChange(timestamp: Long) {
        uiState = uiState.copy(startTimestamp = timestamp)
    }

    fun onEndTimestampChange(timestamp: Long) {
        uiState = uiState.copy(endTimestamp = timestamp)
    }

    fun onGenerateReportClick() {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true)

            val uid = authRepository.getUid().getOrElse { throwable ->
                uiState = uiState.copy(exception = throwable.asException(), loading = false)
                return@launch
            }

            val user = usersRepository.getUserByUid(uid).getOrElse { throwable ->
                uiState = uiState.copy(exception = throwable.asException(), loading = false)
                return@launch
            }

            val contract = uiState.contract ?: run {
                uiState = uiState.copy(
                    exception = IllegalStateException("Contract required"),
                    loading = false
                )
                return@launch
            }

            val actionsResult = actionsRepository.getActions(user.uid).first()
            val actions = actionsResult.getOrElse { throwable ->
                uiState = uiState.copy(exception = throwable.asException(), loading = false)
                return@launch
            }

            val filteredActions = actions.filter { it.timestamp in uiState.startTimestamp..uiState.endTimestamp }
            val reportResult = reportsRepository.generateReport(
                template = uiState.templateFile!!,
                output = uiState.outputFile!!,
                user = user,
                contract = contract,
                startTimestamp = uiState.startTimestamp,
                endTimestamp = uiState.endTimestamp,
                actions = filteredActions
            )

            reportResult
                .onSuccess {
                    uiState = uiState.copy(success = true)
                }
                .onFailure { throwable ->
                    uiState = uiState.copy(exception = throwable.asException())
                }

            uiState = uiState.copy(loading = false)
        }
    }
}

data class GenerateReportUiState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val exception: Exception? = null,
    val templateFile: PlatformFile? = null,
    val outputFile: PlatformFile? = null,
    val contract: Contract? = null,
    val startTimestamp: Long = 0,
    val endTimestamp: Long = 0,
)

private fun Throwable.asException(): Exception = this as? Exception ?: Exception(this)
