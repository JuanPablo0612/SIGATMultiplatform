package com.juanpablo0612.sigat.ui.training_programs.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.juanpablo0612.sigat.data.assistance.AssistanceRepository
import com.juanpablo0612.sigat.data.evaluations.EvaluationsRepository
import com.juanpablo0612.sigat.data.training_programs.TrainingProgramsRepository
import com.juanpablo0612.sigat.data.users.UsersRepository
import com.juanpablo0612.sigat.domain.model.Evaluation
import com.juanpablo0612.sigat.domain.model.TrainingProgram
import com.juanpablo0612.sigat.domain.model.User
import com.juanpablo0612.sigat.ui.navigation.Screen
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock

enum class TrainingProgramDetailTab { Students, Attendance, Evaluations }

class TrainingProgramDetailViewModel(
    private val repository: TrainingProgramsRepository,
    private val assistanceRepository: AssistanceRepository,
    private val evaluationsRepository: EvaluationsRepository,
    private val usersRepository: UsersRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var uiState by mutableStateOf(TrainingProgramDetailUiState())
        private set

    private val trainingProgramDetail = savedStateHandle.toRoute(Screen.TrainingProgramDetail::class)
    private var evaluationsJob: Job? = null

    init {
        loadTrainingProgram(trainingProgramDetail.programId)
    }

    private fun loadTrainingProgram(id: String) {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true, exception = null)
            val program = repository.getTrainingProgram(id).getOrElse { throwable ->
                uiState = uiState.copy(loading = false, exception = throwable.asException())
                return@launch
            } ?: run {
                uiState = uiState.copy(loading = false)
                return@launch
            }

            val students = usersRepository.getUsersByIds(program.students).getOrElse { throwable ->
                uiState = uiState.copy(loading = false, exception = throwable.asException())
                return@launch
            }

            uiState = uiState.copy(
                id = program.id,
                name = program.name,
                code = program.code.toString(),
                startDate = program.startDate,
                endDate = program.endDate,
                schedule = program.schedule,
                teacherUserId = program.teacherUserId,
                students = students,
                loading = false,
                exception = null
            )
            observeEvaluations(program.id)
            loadAttendance(uiState.selectedDate)
        }
    }

    fun loadAttendance(date: Long) {
        viewModelScope.launch {
            uiState = uiState.copy(loadingAttendance = true, selectedDate = date)
            val attendanceResult = assistanceRepository
                .getAssistanceForProgramAndDate(uiState.id, date)
                .first()

            attendanceResult
                .onSuccess { attendance ->
                    uiState = uiState.copy(
                        attendance = attendance.associate { it.studentIdNumber to it.present },
                        loadingAttendance = false,
                        exception = null
                    )
                }
                .onFailure { throwable ->
                    uiState = uiState.copy(
                        loadingAttendance = false,
                        exception = throwable.asException()
                    )
                }
        }
    }

    private fun observeEvaluations(programId: String) {
        evaluationsJob?.cancel()
        uiState = uiState.copy(loadingEvaluations = true)
        evaluationsJob = viewModelScope.launch {
            evaluationsRepository
                .getEvaluationsByTrainingProgram(programId)
                .collect { result ->
                    result
                        .onSuccess { evaluations ->
                            val students = uiState.students
                            val existingGrades = uiState.evaluationGrades
                            val mergedGrades = evaluations.associate { evaluation ->
                                val base = students.associate { it.idNumber to "" }.toMutableMap()
                                evaluation.grades.forEach { (studentId, grade) ->
                                    base[studentId] = grade.toString()
                                }
                                existingGrades[evaluation.id]?.forEach { (studentId, value) ->
                                    base[studentId] = value
                                }
                                evaluation.id to base.toMap()
                            }
                            uiState = uiState.copy(
                                evaluations = evaluations,
                                evaluationGrades = mergedGrades,
                                loadingEvaluations = false,
                                exception = null
                            )
                        }
                        .onFailure { throwable ->
                            uiState = uiState.copy(
                                exception = throwable.asException(),
                                loadingEvaluations = false
                            )
                        }
                }
        }
    }

    fun toggleAttendance(studentId: Long) {
        val current = uiState.attendance[studentId] ?: false
        uiState = uiState.copy(
            attendance = uiState.attendance.toMutableMap().apply { put(studentId, !current) }
        )
    }

    fun saveAttendance() {
        viewModelScope.launch {
            uiState = uiState.copy(loadingAttendance = true)
            val date = uiState.selectedDate
            uiState.attendance.forEach { (studentId, present) ->
                val result = assistanceRepository.setAttendance(uiState.id, studentId, date, present)
                result.onFailure { throwable ->
                    uiState = uiState.copy(
                        exception = throwable.asException(),
                        loadingAttendance = false
                    )
                    return@launch
                }
            }
            uiState = uiState.copy(loadingAttendance = false, exception = null)
        }
    }

    fun onNewEvaluationNameChange(newName: String) {
        uiState = uiState.copy(newEvaluationName = newName, validEvaluationName = true)
    }

    fun onNewEvaluationPercentageChange(newPercentage: String) {
        var sanitized = newPercentage.replace(',', '.')
        if (sanitized.count { it == '.' } > 1) return
        if (sanitized.any { !it.isDigit() && it != '.' }) return
        if (sanitized.startsWith('.')) {
            sanitized = "0$sanitized"
        }
        if (sanitized == ".") {
            sanitized = "0."
        }
        uiState = uiState.copy(newEvaluationPercentage = sanitized, validEvaluationPercentage = true)
    }

    fun addEvaluation() {
        val programId = uiState.id
        val name = uiState.newEvaluationName.trim()
        val percentageText = uiState.newEvaluationPercentage.trim()
        val percentage = percentageText.toDoubleOrNull()
        val validName = name.isNotEmpty()
        val validPercentage = percentage != null && percentage in 0.0..100.0

        uiState = uiState.copy(
            validEvaluationName = validName,
            validEvaluationPercentage = validPercentage
        )

        if (!validName || !validPercentage || programId.isEmpty()) return

        viewModelScope.launch {
            uiState = uiState.copy(loadingEvaluations = true)
            val result = evaluationsRepository.addEvaluation(
                Evaluation(
                    trainingProgramId = programId,
                    name = name,
                    percentage = percentage
                )
            )
            result
                .onSuccess {
                    uiState = uiState.copy(
                        newEvaluationName = "",
                        newEvaluationPercentage = "",
                        validEvaluationName = true,
                        validEvaluationPercentage = true,
                        loadingEvaluations = false,
                        exception = null
                    )
                }
                .onFailure { throwable ->
                    uiState = uiState.copy(
                        exception = throwable.asException(),
                        loadingEvaluations = false
                    )
                }
        }
    }

    fun onEvaluationGradeChange(evaluationId: String, studentId: Long, value: String) {
        var sanitized = value.replace(',', '.')
        if (sanitized.count { it == '.' } > 1) return
        if (sanitized.any { !it.isDigit() && it != '.' }) return
        if (sanitized.startsWith('.')) {
            sanitized = "0$sanitized"
        }
        if (sanitized == ".") {
            sanitized = "0."
        }

        val updatedEvaluations = uiState.evaluationGrades.toMutableMap()
        val evaluationGrades = updatedEvaluations[evaluationId]?.toMutableMap() ?: mutableMapOf()

        if (sanitized.isEmpty()) {
            evaluationGrades.remove(studentId)
        } else {
            evaluationGrades[studentId] = sanitized
        }

        updatedEvaluations[evaluationId] = evaluationGrades.toMap()
        uiState = uiState.copy(evaluationGrades = updatedEvaluations)
    }

    fun saveEvaluationGrades(evaluationId: String) {
        val grades = uiState.evaluationGrades[evaluationId].orEmpty()
        val parsedGrades = mutableMapOf<Long, Double>()
        grades.forEach { (studentId, text) ->
            val cleaned = text.trim().trimEnd('.')
            val parsed = cleaned.toDoubleOrNull()
            if (parsed != null) {
                parsedGrades[studentId] = parsed
            }
        }

        viewModelScope.launch {
            uiState = uiState.copy(loadingEvaluations = true)
            val result = evaluationsRepository.setGrades(evaluationId, parsedGrades)
            result
                .onSuccess {
                    uiState = uiState.copy(loadingEvaluations = false, exception = null)
                }
                .onFailure { throwable ->
                    uiState = uiState.copy(
                        loadingEvaluations = false,
                        exception = throwable.asException()
                    )
                }
        }
    }

    fun deleteEvaluation(evaluationId: String) {
        viewModelScope.launch {
            uiState = uiState.copy(loadingEvaluations = true)
            val result = evaluationsRepository.deleteEvaluation(evaluationId)
            result
                .onSuccess {
                    uiState = uiState.copy(
                        evaluationGrades = uiState.evaluationGrades - evaluationId,
                        loadingEvaluations = false,
                        exception = null
                    )
                }
                .onFailure { throwable ->
                    uiState = uiState.copy(
                        loadingEvaluations = false,
                        exception = throwable.asException()
                    )
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

            val result = repository.updateTrainingProgram(
                TrainingProgram(
                    id = uiState.id,
                    name = uiState.name,
                    code = uiState.code.toInt(),
                    startDate = uiState.startDate!!,
                    endDate = uiState.endDate!!,
                    schedule = uiState.schedule,
                    teacherUserId = uiState.teacherUserId,
                    students = uiState.students.map { it.idNumber }
                )
            )
            result.onFailure { throwable ->
                uiState = uiState.copy(exception = throwable.asException())
            }
        }
    }

    fun deleteTrainingProgram() {
        viewModelScope.launch {
            val result = repository.deleteTrainingProgram(uiState.id)
            result
                .onSuccess {
                    uiState = uiState.copy(finished = true, exception = null)
                }
                .onFailure { throwable ->
                    uiState = uiState.copy(exception = throwable.asException())
                }
        }
    }

    fun addStudent() {
        viewModelScope.launch {
            val result = repository.addStudentToTrainingProgram(uiState.id, uiState.newStudentId)
            result
                .onSuccess {
                    loadTrainingProgram(uiState.id)
                    uiState = uiState.copy(newStudentId = "", exception = null)
                }
                .onFailure { throwable ->
                    uiState = uiState.copy(exception = throwable.asException())
                }
        }
    }

    fun removeStudent(studentIdNumber: Long) {
        viewModelScope.launch {
            val result = repository.removeStudentFromTrainingProgram(uiState.id, studentIdNumber)
            result
                .onSuccess {
                    loadTrainingProgram(uiState.id)
                    uiState = uiState.copy(exception = null)
                }
                .onFailure { throwable ->
                    uiState = uiState.copy(exception = throwable.asException())
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
    val students: List<User> = emptyList(),
    val evaluations: List<Evaluation> = emptyList(),
    val evaluationGrades: Map<String, Map<Long, String>> = emptyMap(),
    val newEvaluationName: String = "",
    val newEvaluationPercentage: String = "",
    val validEvaluationName: Boolean = true,
    val validEvaluationPercentage: Boolean = true,
    val newStudentId: String = "",
    val loading: Boolean = false,
    val loadingEvaluations: Boolean = false,
    val selectedTab: TrainingProgramDetailTab = TrainingProgramDetailTab.Students,
    val selectedDate: Long = Clock.System.now().toEpochMilliseconds(),
    val attendance: Map<Long, Boolean> = emptyMap(),
    val loadingAttendance: Boolean = false,
    val exception: Exception? = null,
    val finished: Boolean = false
)

private fun Throwable.asException(): Exception = this as? Exception ?: Exception(this)
