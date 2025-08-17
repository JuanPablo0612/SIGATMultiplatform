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
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class GenerateReportViewModel(
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository,
    private val actionsRepository: ActionsRepository,
    private val reportsRepository: ReportsRepository
) : ViewModel() {
    var uiState by mutableStateOf((GenerateReportUiState()))
        private set

    fun onTemplateFileSelected(file: PlatformFile) {
        uiState = uiState.copy(templateFile = file)
    }

    fun onOutputFileSelected(file: PlatformFile) {
        uiState = uiState.copy(outputFile = file)
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
                        reportsRepository.generateReport(
                            template = uiState.templateFile!!,
                            output = uiState.outputFile!!,
                            user = user,
                            actions = actions
                        )
                    },
                    onFailure = { uiState = uiState.copy(exception = it as Exception) }
                )
            } catch (e: Exception) {
                uiState = uiState.copy(exception = e)
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
    val contractNumber: String = "",
    val startTimestamp: Long = 0,
    val endTimestamp: Long = 0,
)