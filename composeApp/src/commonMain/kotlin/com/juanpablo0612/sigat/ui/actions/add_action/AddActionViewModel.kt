package com.juanpablo0612.sigat.ui.actions.add_action

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanpablo0612.sigat.data.auth.AuthRepository
import com.juanpablo0612.sigat.data.obligations.ObligationsRepository
import com.juanpablo0612.sigat.data.users.UsersRepository
import com.juanpablo0612.sigat.domain.model.Obligation
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.launch

class AddActionViewModel(
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
            val user = usersRepository.getUserByUid(uid)
            val obligations = obligationsRepository.getObligations(user.role.id)
            uiState = uiState.copy(obligations = obligations.sortedBy { it.number })
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

    fun onImageSourceSelectorVisibilityChange(newVisibility: Boolean) {
        uiState = uiState.copy(showImageSourceSelector = newVisibility)
    }

    fun onAddImages(selectedImages: List<PlatformFile>) {
        val images = uiState.images.toMutableList()
        images.addAll(selectedImages)
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
    val showImageSourceSelector: Boolean = false,
    val images: List<PlatformFile> = emptyList(),
    val success: Boolean = false,
    val loading: Boolean = false,
    val exception: Exception? = null
)