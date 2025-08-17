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
    }

    fun onCodeChange(newCode: String) {
        uiState = uiState.copy(code = newCode, validCode = newCode.toIntOrNull() != null)
    }

    fun onStartDateChange(newStartDate: String) {
        val valid = newStartDate.toLongOrNull() != null
        val validEnd = uiState.endDate.toLongOrNull()?.let { end ->
            newStartDate.toLongOrNull()?.let { start -> start <= end } ?: false
        } ?: true
        uiState = uiState.copy(startDate = newStartDate, validStartDate = valid && validEnd)
        if (!validEnd) {
            uiState = uiState.copy(validEndDate = false)
        }
    }

    fun onEndDateChange(newEndDate: String) {
        val endLong = newEndDate.toLongOrNull()
        val startLong = uiState.startDate.toLongOrNull()
        val valid = endLong != null && (startLong == null || startLong <= endLong)
        uiState = uiState.copy(endDate = newEndDate, validEndDate = valid)
    }

    fun onScheduleChange(newSchedule: String) {
        uiState = uiState.copy(schedule = newSchedule, validSchedule = newSchedule.isNotBlank())
    }

    private fun validateFields() {
        onNameChange(uiState.name)
        onCodeChange(uiState.code)
        onStartDateChange(uiState.startDate)
        onEndDateChange(uiState.endDate)
        onScheduleChange(uiState.schedule)
    }

    private fun allFieldsValid(): Boolean {
        return uiState.validName && uiState.validCode && uiState.validStartDate && uiState.validEndDate && uiState.validSchedule
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
                        startDate = uiState.startDate.toLong(),
                        endDate = uiState.endDate.toLong(),
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
    val startDate: String = "",
    val validStartDate: Boolean = true,
    val endDate: String = "",
    val validEndDate: Boolean = true,
    val schedule: String = "",
    val validSchedule: Boolean = true,
    val loading: Boolean = false,
    val exception: Exception? = null,
    val saved: Boolean = false
)
