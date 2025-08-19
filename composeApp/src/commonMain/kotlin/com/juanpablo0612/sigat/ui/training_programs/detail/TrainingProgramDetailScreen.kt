package com.juanpablo0612.sigat.ui.training_programs.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.juanpablo0612.sigat.ui.components.LoadingContent
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.button_delete
import sigat.composeapp.generated.resources.button_save
import sigat.composeapp.generated.resources.code_label
import sigat.composeapp.generated.resources.end_date_label
import sigat.composeapp.generated.resources.name_label
import sigat.composeapp.generated.resources.schedule_label
import sigat.composeapp.generated.resources.start_date_label
import sigat.composeapp.generated.resources.student_id_label
import sigat.composeapp.generated.resources.training_program_detail_title
import com.juanpablo0612.sigat.ui.components.DatePickerTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingProgramDetailScreen(
    viewModel: TrainingProgramDetailViewModel = koinViewModel(),
    windowSize: WindowSizeClass,
    onNavigateBack: () -> Unit
) {
    val uiState = viewModel.uiState
    val startDatePickerState = rememberDatePickerState(initialSelectedDateMillis = uiState.startDate)
    val endDatePickerState = rememberDatePickerState(initialSelectedDateMillis = uiState.endDate)

    if (uiState.finished) {
        LaunchedEffect(Unit) { onNavigateBack() }
    }
    LaunchedEffect(startDatePickerState.selectedDateMillis) {
        startDatePickerState.selectedDateMillis?.let {
            viewModel.onStartDateChange(it)
        }
    }

    LaunchedEffect(endDatePickerState.selectedDateMillis) {
        endDatePickerState.selectedDateMillis?.let {
            viewModel.onEndDateChange(it)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.training_program_detail_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack, enabled = !uiState.loading) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (uiState.id.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                item {
                    OutlinedTextField(
                        value = uiState.name,
                        onValueChange = viewModel::onNameChange,
                        label = { Text(stringResource(Res.string.name_label)) },
                        modifier = Modifier.fillMaxWidth(),
                        isError = !uiState.validName,
                        singleLine = true,
                        enabled = !uiState.loading,
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                    )
                }
                item {
                    OutlinedTextField(
                        value = uiState.code,
                        onValueChange = viewModel::onCodeChange,
                        label = { Text(stringResource(Res.string.code_label)) },
                        modifier = Modifier.fillMaxWidth(),
                        isError = !uiState.validCode,
                        singleLine = true,
                        enabled = !uiState.loading,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                item {
                    DatePickerTextField(
                        label = stringResource(Res.string.start_date_label),
                        state = startDatePickerState,
                        isError = !uiState.validStartDate,
                        enabled = !uiState.loading
                    )
                }
                item {
                    DatePickerTextField(
                        label = stringResource(Res.string.end_date_label),
                        state = endDatePickerState,
                        isError = !uiState.validEndDate,
                        enabled = !uiState.loading
                    )
                }

                item {
                    OutlinedTextField(
                        value = uiState.schedule,
                        onValueChange = viewModel::onScheduleChange,
                        label = { Text(stringResource(Res.string.schedule_label)) },
                        modifier = Modifier.fillMaxWidth(),
                        isError = !uiState.validSchedule,
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                        enabled = !uiState.loading
                    )
                }

                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = uiState.newStudentId,
                            onValueChange = viewModel::onStudentIdChange,
                            label = { Text(stringResource(Res.string.student_id_label)) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            enabled = !uiState.loading
                        )
                        IconButton(onClick = { viewModel.addStudent() }, enabled = !uiState.loading) {
                            Icon(Icons.Filled.Add, contentDescription = null)
                        }
                    }
                }

                items(
                    items = uiState.students,
                    key = { it }
                ) { student ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = student, fontWeight = FontWeight.Bold)
                        IconButton(onClick = { viewModel.removeStudent(student) }, enabled = !uiState.loading) {
                            Icon(Icons.Filled.Delete, contentDescription = null)
                        }
                    }
                }

                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { viewModel.updateTrainingProgram() },
                            modifier = Modifier.weight(1f),
                            enabled = !uiState.loading
                        ) {
                            Text(stringResource(Res.string.button_save))
                        }
                        OutlinedButton(
                            onClick = { viewModel.deleteTrainingProgram() },
                            modifier = Modifier.weight(1f),
                            enabled = !uiState.loading
                        ) {
                            Text(stringResource(Res.string.button_delete))
                        }
                    }
                }
            }
            }
            if (uiState.loading) {
                LoadingContent(modifier = Modifier.matchParentSize())
            }
        }
    }
}
