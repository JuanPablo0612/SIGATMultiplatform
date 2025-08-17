package com.juanpablo0612.sigat.ui.actions.add_action

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanpablo0612.sigat.data.actions.ActionsRepository
import com.juanpablo0612.sigat.data.auth.AuthRepository
import com.juanpablo0612.sigat.data.obligations.ObligationsRepository
import com.juanpablo0612.sigat.data.users.UsersRepository
import com.juanpablo0612.sigat.domain.model.Action
import com.juanpablo0612.sigat.domain.model.Obligation
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.launch

class AddActionViewModel(
    private val actionsRepository: ActionsRepository,
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository,
    private val obligationsRepository: ObligationsRepository
) : ViewModel() {
    var uiState by mutableStateOf(AddActionUiState())
        private set

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            val uid = authRepository.getUid()
            val userResult = usersRepository.getUserByUid(uid)
            userResult.fold(
                onSuccess = { user ->
                    val obligations = obligationsRepository.getObligations(user.role.id)
                    uiState = uiState.copy(
                        uid = uid,
                        obligations = obligations.sortedBy { it.number }
                    )
                },
                onFailure = { uiState = uiState.copy(exception = it as Exception) }
            )
        }
    }

    fun onObligationChange(newObligation: Obligation) {
        uiState = uiState.copy(obligation = newObligation)
    }

    fun onExpandObligationListChange(newExpand: Boolean) {
        uiState = uiState.copy(expandObligationList = newExpand)
    }

    fun onDescriptionChange(newDescription: String) {
        uiState = uiState.copy(description = newDescription)
        validateAction()
    }

    fun onTimestampChange(newTimestamp: Long) {
        uiState = uiState.copy(timestamp = newTimestamp)
    }

    fun onDatePickerVisibilityChange(newVisibility: Boolean) {
        uiState = uiState.copy(showDatePicker = newVisibility)
    }

    fun onAddImages(selectedImages: List<PlatformFile>) {
        val images = uiState.images.toMutableList()
        selectedImages.forEach {
            if (!images.contains(it)) {
                images.add(it)
            }
        }
        uiState = uiState.copy(images = images)
    }

    fun onDeleteImage(image: PlatformFile) {
        val images = uiState.images.toMutableList()
        images.remove(image)
        uiState = uiState.copy(images = images)
    }

    private fun validateAction() {
        val validAction = uiState.description.isNotBlank()
        uiState = uiState.copy(validDescription = validAction)
    }

    fun onAdd() {
        viewModelScope.launch {
            viewModelScope.launch {
                uiState = uiState.copy(loading = true)

                try {
                    val action = Action(
                        creatorUid = authRepository.getUid(),
                        obligationNumber = uiState.obligation!!.number,
                        obligationName = uiState.obligation!!.name,
                        description = uiState.description,
                        timestamp = uiState.timestamp!!,
                        images = emptyList()
                    )

                    actionsRepository.addAction(action, uiState.images)
                    uiState = uiState.copy(success = true)
                } catch (e: Exception) {
                    uiState = uiState.copy(exception = e)
                } finally {
                    uiState = uiState.copy(loading = false)
                }
            }
        }
    }
}

data class AddActionUiState(
    val uid: String = "",
    val obligations: List<Obligation> = emptyList(),
    val expandObligationList: Boolean = false,
    val obligation: Obligation? = null,
    val validObligation: Boolean = false,
    val description: String = "",
    val validDescription: Boolean = false,
    val showDatePicker: Boolean = false,
    val timestamp: Long? = null,
    val images: List<PlatformFile> = emptyList(),
    val success: Boolean = false,
    val loading: Boolean = false,
    val exception: Exception? = null
)