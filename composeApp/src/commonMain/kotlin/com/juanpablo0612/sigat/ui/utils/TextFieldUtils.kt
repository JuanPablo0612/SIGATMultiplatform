package com.juanpablo0612.sigat.ui.utils

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

fun TextFieldState.observeValidation(
    viewModelScope: CoroutineScope,
    validator: (String) -> Boolean,
    updateState: (Boolean) -> Unit
) {
    viewModelScope.launch {
        snapshotFlow { this@observeValidation.text }
            .drop(1)
            .distinctUntilChanged()
            .map { validator(it.toString()) }
            .collectLatest(updateState)
    }
}