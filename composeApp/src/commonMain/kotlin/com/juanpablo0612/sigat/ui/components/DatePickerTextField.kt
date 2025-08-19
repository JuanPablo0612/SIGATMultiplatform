package com.juanpablo0612.sigat.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.button_accept
import sigat.composeapp.generated.resources.button_cancel
import com.juanpablo0612.sigat.utils.timestampToDayMonthYearFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerTextField(
    label: String,
    state: DatePickerState,
    isError: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    var showDialog by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = state.selectedDateMillis?.let { timestampToDayMonthYearFormat(it) } ?: "",
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        enabled = enabled,
        trailingIcon = {
            IconButton(onClick = { showDialog = true }, enabled = enabled) {
                Icon(Icons.Filled.DateRange, contentDescription = null)
            }
        },
        modifier = modifier.fillMaxWidth(),
        isError = isError,
        singleLine = true
    )

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(Res.string.button_accept))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(Res.string.button_cancel))
                }
            }
        ) {
            DatePicker(state = state)
        }
    }
}

