package com.juanpablo0612.sigat.ui.training_programs.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.add_training_program_title
import sigat.composeapp.generated.resources.button_save
import sigat.composeapp.generated.resources.code_label
import sigat.composeapp.generated.resources.end_date_label
import sigat.composeapp.generated.resources.name_label
import sigat.composeapp.generated.resources.start_date_label
import sigat.composeapp.generated.resources.schedule_label
import com.juanpablo0612.sigat.ui.components.DatePickerTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTrainingProgramScreen(
    viewModel: AddTrainingProgramViewModel = koinViewModel(),
    windowSize: WindowSizeClass,
    onNavigateBack: () -> Unit
) {
    val uiState = viewModel.uiState
    val startDatePickerState = rememberDatePickerState(initialSelectedDateMillis = uiState.startDate)
    val endDatePickerState = rememberDatePickerState(initialSelectedDateMillis = uiState.endDate)

    LaunchedEffect(startDatePickerState.selectedDateMillis) {
        startDatePickerState.selectedDateMillis?.let { viewModel.onStartDateChange(it) }
    }

    LaunchedEffect(endDatePickerState.selectedDateMillis) {
        endDatePickerState.selectedDateMillis?.let { viewModel.onEndDateChange(it) }
    }

    if (uiState.saved) {
        LaunchedEffect(Unit) { onNavigateBack() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.add_training_program_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = uiState.name,
                onValueChange = viewModel::onNameChange,
                label = { Text(stringResource(Res.string.name_label)) },
                modifier = Modifier.fillMaxWidth(),
                isError = !uiState.validName
            )
            OutlinedTextField(
                value = uiState.code,
                onValueChange = viewModel::onCodeChange,
                label = { Text(stringResource(Res.string.code_label)) },
                modifier = Modifier.fillMaxWidth(),
                isError = !uiState.validCode
            )
            DatePickerTextField(
                label = stringResource(Res.string.start_date_label),
                state = startDatePickerState,
                isError = !uiState.validStartDate
            )
            DatePickerTextField(
                label = stringResource(Res.string.end_date_label),
                state = endDatePickerState,
                isError = !uiState.validEndDate
            )
            OutlinedTextField(
                value = uiState.schedule,
                onValueChange = viewModel::onScheduleChange,
                label = { Text(stringResource(Res.string.schedule_label)) },
                modifier = Modifier.fillMaxWidth(),
                isError = !uiState.validSchedule
            )
            Button(
                onClick = { viewModel.addTrainingProgram() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(Res.string.button_save))
            }
        }
    }
}
