package com.juanpablo0612.sigat.ui.actions.action_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanpablo0612.sigat.data.actions.ActionsRepository
import com.juanpablo0612.sigat.domain.model.Action
import com.juanpablo0612.sigat.state_holders.UserStateHolder
import kotlinx.coroutines.launch

class ActionListViewModel(
    private val actionsRepository: ActionsRepository,
    private val userStateHolder: UserStateHolder,
) : ViewModel() {
    var uiState by mutableStateOf(ActionListUiState())
        private set

    init {
        loadActions()
    }

    private fun loadActions() {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true)

            try {
                val uid = userStateHolder.userState.user?.uid ?: return@launch
                actionsRepository.getActions(uid).collect {
                    uiState = uiState.copy(actions = it, loading = false)
                }
            } catch (e: Exception) {
                uiState = uiState.copy(exception = e)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }
}

data class ActionListUiState(
    val loading: Boolean = false,
    val exception: Exception? = null,
    val actions: List<Action> = emptyList()
)