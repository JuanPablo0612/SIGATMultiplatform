package com.juanpablo0612.sigat.ui.training_programs.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanpablo0612.sigat.data.training_programs.TrainingProgramsRepository
import com.juanpablo0612.sigat.domain.model.TrainingProgram
import com.juanpablo0612.sigat.state_holders.UserStateHolder
import kotlinx.coroutines.launch

class AddTrainingProgramViewModel(
    private val repository: TrainingProgramsRepository,
    private val userStateHolder: UserStateHolder,
) : ViewModel() {
    var uiState by mutableStateOf(AddTrainingProgramUiState())
        private set

    fun onNameChange(newName: String) {
        uiState = uiState.copy(name = newName, validName = newName.isNotBlank())
        validateName()
    }

    fun onCodeChange(newCode: String) {
        uiState = uiState.copy(code = newCode, validCode = newCode.toIntOrNull() != null)
        validateCode()
    }

    fun onStartDateChange(newStartDate: Long) {
        uiState = uiState.copy(startDate = newStartDate)
        validateDates()
    }

    fun onEndDateChange(newEndDate: Long) {
        uiState = uiState.copy(endDate = newEndDate)
        validateDates()
    }

    fun onScheduleChange(newSchedule: String) {
        uiState = uiState.copy(schedule = newSchedule, validSchedule = newSchedule.isNotBlank())
        validateSchedule()
    }

    private fun validateName() {
        uiState = uiState.copy(validName = uiState.name.isNotBlank())
    }

    private fun validateCode() {
        uiState = uiState.copy(validCode = uiState.code.toIntOrNull() != null)
    }

    private fun validateSchedule() {
        uiState = uiState.copy(validSchedule = uiState.schedule.isNotBlank())
    }

    private fun validateFields() {
        validateName()
        validateCode()
        validateDates()
        validateSchedule()
    }

    private fun allFieldsValid(): Boolean {
        return uiState.validName && uiState.validCode && uiState.validStartDate && uiState.validEndDate && uiState.validSchedule
    }

    private fun validateDates() {
        val start = uiState.startDate
        val end = uiState.endDate
        val orderValid = if (start != null && end != null) start <= end else true
        uiState = uiState.copy(
            validStartDate = start != null && orderValid,
            validEndDate = end != null && orderValid
        )
    }

    fun addTrainingProgram() {
        viewModelScope.launch {
            validateFields()

            if (!allFieldsValid()) return@launch

            uiState = uiState.copy(loading = true)
            try {
                val teacherId = userStateHolder.userState.user?.uid ?: return@launch
                repository.createTrainingProgram(
                    TrainingProgram(
                        name = uiState.name,
                        code = uiState.code.toInt(),
                        startDate = uiState.startDate!!,
                        endDate = uiState.endDate!!,
                        schedule = uiState.schedule,
                        teacherUserId = teacherId
                    )
                )
                uiState = uiState.copy(saved = true)
            } catch (e: Exception) {
                uiState = uiState.copy(exception = e)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }
}

data class AddTrainingProgramUiState(
    val name: String = "",
    val validName: Boolean = true,
    val code: String = "",
    val validCode: Boolean = true,
    val startDate: Long? = null,
    val validStartDate: Boolean = true,
    val endDate: Long? = null,
    val validEndDate: Boolean = true,
    val schedule: String = "",
    val validSchedule: Boolean = true,
    val loading: Boolean = false,
    val exception: Exception? = null,
    val saved: Boolean = false
)
