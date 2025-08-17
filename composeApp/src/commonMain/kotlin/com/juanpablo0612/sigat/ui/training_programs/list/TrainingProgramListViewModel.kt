package com.juanpablo0612.sigat.ui.training_programs.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanpablo0612.sigat.data.training_programs.TrainingProgramsRepository
import com.juanpablo0612.sigat.domain.model.TrainingProgram
import com.juanpablo0612.sigat.state_holders.UserStateHolder
import kotlinx.coroutines.launch

class TrainingProgramListViewModel(
    private val repository: TrainingProgramsRepository,
    private val userStateHolder: UserStateHolder,
) : ViewModel() {
    var uiState by mutableStateOf(TrainingProgramListUiState())
        private set

    init {
        loadPrograms()
    }

    private fun loadPrograms() {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true)
            try {
                val teacherId = userStateHolder.userState.user?.uid ?: return@launch
                repository.getTrainingProgramsByTeacherId(teacherId).collect {
                    uiState = uiState.copy(programs = it, loading = false)
                }
            } catch (e: Exception) {
                uiState = uiState.copy(exception = e)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }
}

data class TrainingProgramListUiState(
    val programs: List<TrainingProgram> = emptyList(),
    val loading: Boolean = false,
    val exception: Exception? = null
)
