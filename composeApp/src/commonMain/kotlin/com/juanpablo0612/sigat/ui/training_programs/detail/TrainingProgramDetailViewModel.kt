package com.juanpablo0612.sigat.ui.training_programs.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.juanpablo0612.sigat.data.assistance.AssistanceRepository
import com.juanpablo0612.sigat.data.training_programs.TrainingProgramsRepository
import com.juanpablo0612.sigat.data.users.UsersRepository
import com.juanpablo0612.sigat.domain.model.TrainingProgram
import com.juanpablo0612.sigat.domain.model.User
import com.juanpablo0612.sigat.ui.navigation.Screen
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock

enum class TrainingProgramDetailTab { Students, Attendance }

class TrainingProgramDetailViewModel(
    private val repository: TrainingProgramsRepository,
    private val assistanceRepository: AssistanceRepository,
    private val usersRepository: UsersRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var uiState by mutableStateOf(TrainingProgramDetailUiState())
        private set

    private val trainingProgramDetail = savedStateHandle.toRoute(Screen.TrainingProgramDetail::class)

    init {
        loadTrainingProgram(trainingProgramDetail.programId)
    }

    private fun loadTrainingProgram(id: String) {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true)
            try {
                val program = repository.getTrainingProgram(id)
                if (program != null) {
                    val students = usersRepository.getUsersByIds(program.students).getOrThrow()
                    uiState = uiState.copy(
                        id = program.id,
                        name = program.name,
                        code = program.code.toString(),
                        startDate = program.startDate,
                        endDate = program.endDate,
                        schedule = program.schedule,
                        teacherUserId = program.teacherUserId,
                        studentIds = program.students,
                        students = students
                    )
                    loadAttendance(uiState.selectedDate)
                }
            } catch (e: Exception) {
                uiState = uiState.copy(exception = e)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    fun loadAttendance(date: Long) {
        viewModelScope.launch {
            uiState = uiState.copy(loadingAttendance = true, selectedDate = date)
            try {
                val attendance = assistanceRepository
                    .getAssistanceForProgramAndDate(uiState.id, date)
                    .first()
                uiState = uiState.copy(
                    attendance = attendance.associate { it.studentId to it.present }
                )
            } catch (e: Exception) {
                uiState = uiState.copy(exception = e)
            } finally {
                uiState = uiState.copy(loadingAttendance = false)
            }
        }
    }

    fun toggleAttendance(studentId: String) {
        val current = uiState.attendance[studentId] ?: false
        uiState = uiState.copy(
            attendance = uiState.attendance.toMutableMap().apply { put(studentId, !current) }
        )
    }

    fun saveAttendance() {
        viewModelScope.launch {
            uiState = uiState.copy(loadingAttendance = true)
            try {
                val date = uiState.selectedDate
                uiState.attendance.forEach { (studentId, present) ->
                    assistanceRepository.setAttendance(uiState.id, studentId, date, present)
                }
            } catch (e: Exception) {
                uiState = uiState.copy(exception = e)
            } finally {
                uiState = uiState.copy(loadingAttendance = false)
            }
        }
    }

    fun onTabChange(tab: TrainingProgramDetailTab) {
        uiState = uiState.copy(selectedTab = tab)
    }
    fun onNameChange(newName: String) {
        uiState = uiState.copy(name = newName, validName = newName.isNotBlank())
    }

    fun onCodeChange(newCode: String) {
        uiState = uiState.copy(code = newCode, validCode = newCode.toIntOrNull() != null)
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
    }

    fun onStudentIdChange(newId: String) {
        uiState = uiState.copy(newStudentId = newId)
    }

    private fun validateFields() {
        onNameChange(uiState.name)
        onCodeChange(uiState.code)
        validateDates()
        onScheduleChange(uiState.schedule)
    }

    private fun allFieldsValid(): Boolean {
        return uiState.validName && uiState.validCode && uiState.validStartDate && uiState.validEndDate && uiState.validSchedule
    }

    fun updateTrainingProgram() {
        viewModelScope.launch {
            validateFields()

            if (!allFieldsValid()) return@launch

            try {
                repository.updateTrainingProgram(
                    TrainingProgram(
                        id = uiState.id,
                        name = uiState.name,
                        code = uiState.code.toInt(),
                        startDate = uiState.startDate!!,
                        endDate = uiState.endDate!!,
                        schedule = uiState.schedule,
                        teacherUserId = uiState.teacherUserId,
                        students = uiState.studentIds
                    )
                )
            } catch (e: Exception) {
                uiState = uiState.copy(exception = e)
            }
        }
    }

    fun deleteTrainingProgram() {
        viewModelScope.launch {
            try {
                repository.deleteTrainingProgram(uiState.id)
                uiState = uiState.copy(finished = true)
            } catch (e: Exception) {
                uiState = uiState.copy(exception = e)
            }
        }
    }

    fun addStudent() {
        viewModelScope.launch {
            try {
                repository.addStudentToTrainingProgram(uiState.id, uiState.newStudentId)
                loadTrainingProgram(uiState.id)
                uiState = uiState.copy(newStudentId = "")
            } catch (e: Exception) {
                uiState = uiState.copy(exception = e)
            }
        }
    }

    fun removeStudent(studentId: String) {
        viewModelScope.launch {
            try {
                repository.removeStudentFromTrainingProgram(uiState.id, studentId)
                loadTrainingProgram(uiState.id)
            } catch (e: Exception) {
                uiState = uiState.copy(exception = e)
            }
        }
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
}

data class TrainingProgramDetailUiState(
    val id: String = "",
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
    val teacherUserId: String = "",
    val studentIds: List<String> = emptyList(),
    val students: List<User> = emptyList(),
    val newStudentId: String = "",
    val loading: Boolean = false,
    val selectedTab: TrainingProgramDetailTab = TrainingProgramDetailTab.Students,
    val selectedDate: Long = Clock.System.now().toEpochMilliseconds(),
    val attendance: Map<String, Boolean> = emptyMap(),
    val loadingAttendance: Boolean = false,
    val exception: Exception? = null,
    val finished: Boolean = false
)
