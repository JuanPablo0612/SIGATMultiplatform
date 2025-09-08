package com.juanpablo0612.sigat.ui.training_programs.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.juanpablo0612.sigat.ui.components.DatePickerTextField
import com.juanpablo0612.sigat.ui.components.LoadingContent
import com.juanpablo0612.sigat.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.button_add_student
import sigat.composeapp.generated.resources.button_delete
import sigat.composeapp.generated.resources.button_save
import sigat.composeapp.generated.resources.code_label
import sigat.composeapp.generated.resources.content_description_collapse
import sigat.composeapp.generated.resources.content_description_expand
import sigat.composeapp.generated.resources.end_date_label
import sigat.composeapp.generated.resources.name_label
import sigat.composeapp.generated.resources.schedule_label
import sigat.composeapp.generated.resources.start_date_label
import sigat.composeapp.generated.resources.student_id_label

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingProgramDetailScreen(
    viewModel: TrainingProgramDetailViewModel = koinViewModel(),
    windowSize: WindowSizeClass,
    onNavigateBack: () -> Unit
) {
    val uiState = viewModel.uiState
    val startDatePickerState =
        rememberDatePickerState(initialSelectedDateMillis = uiState.startDate)
    val endDatePickerState = rememberDatePickerState(initialSelectedDateMillis = uiState.endDate)
    val expandedFields = remember { mutableStateOf(false) }

    if (uiState.finished) {
        LaunchedEffect(Unit) { onNavigateBack() }
    }

    LaunchedEffect(uiState.id) {
        if (uiState.id.isNotEmpty()) {
            startDatePickerState.selectedDateMillis = uiState.startDate
            endDatePickerState.selectedDateMillis = uiState.endDate
        }
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
            TrainingProgramDetailTopAppBar(onNavigateBack = onNavigateBack)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            if (uiState.id.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .widthIn(max = 400.dp)
                        .fillMaxSize()
                        .padding(vertical = Dimens.PaddingMedium),
                    verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall),
                    contentPadding = PaddingValues(bottom = Dimens.PaddingMedium)
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = Dimens.PaddingSmall)
                                .padding(Dimens.PaddingExtraSmall),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${uiState.code} - ${uiState.name}",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { expandedFields.value = !expandedFields.value }) {
                                Icon(
                                    imageVector = if (expandedFields.value) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                    contentDescription = stringResource(
                                        if (expandedFields.value) Res.string.content_description_collapse else Res.string.content_description_expand
                                    )
                                )
                            }
                        }
                    }
                    item {
                        AnimatedVisibility(
                            visible = expandedFields.value,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(Dimens.PaddingSmall)
                                    .padding(bottom = Dimens.PaddingSmall),
                                verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall)
                            ) {
                                OutlinedTextField(
                                    value = uiState.name,
                                    onValueChange = viewModel::onNameChange,
                                    label = { Text(stringResource(Res.string.name_label)) },
                                    modifier = Modifier.fillMaxWidth(),
                                    isError = !uiState.validName,
                                    singleLine = true,
                                    enabled = !uiState.loading,
                                    keyboardOptions = KeyboardOptions(
                                        capitalization = KeyboardCapitalization.Sentences,
                                        imeAction = ImeAction.Next
                                    )
                                )
                                OutlinedTextField(
                                    value = uiState.code,
                                    onValueChange = viewModel::onCodeChange,
                                    label = { Text(stringResource(Res.string.code_label)) },
                                    modifier = Modifier.fillMaxWidth(),
                                    isError = !uiState.validCode,
                                    singleLine = true,
                                    enabled = !uiState.loading,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Next
                                    )
                                )
                                OutlinedTextField(
                                    value = uiState.schedule,
                                    onValueChange = viewModel::onScheduleChange,
                                    label = { Text(stringResource(Res.string.schedule_label)) },
                                    modifier = Modifier.fillMaxWidth(),
                                    isError = !uiState.validSchedule,
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        capitalization = KeyboardCapitalization.Sentences,
                                        imeAction = ImeAction.Done
                                    ),
                                    enabled = !uiState.loading
                                )
                                DatePickerTextField(
                                    label = stringResource(Res.string.start_date_label),
                                    state = startDatePickerState,
                                    isError = !uiState.validStartDate,
                                    enabled = !uiState.loading
                                )
                                DatePickerTextField(
                                    label = stringResource(Res.string.end_date_label),
                                    state = endDatePickerState,
                                    isError = !uiState.validEndDate,
                                    enabled = !uiState.loading
                                )
                            }
                        }
                    }
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall)
                        ) {
                            OutlinedTextField(
                                value = uiState.newStudentId,
                                onValueChange = viewModel::onStudentIdChange,
                                label = { Text(stringResource(Res.string.student_id_label)) },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                ),
                                enabled = !uiState.loading
                            )
                            IconButton(
                                onClick = { viewModel.addStudent() },
                                enabled = !uiState.loading
                            ) {
                                Icon(
                                    Icons.Filled.Add,
                                    contentDescription = stringResource(Res.string.button_add_student)
                                )
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
                                .padding(vertical = Dimens.PaddingExtraSmall),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = student, fontWeight = FontWeight.Bold)
                            IconButton(
                                onClick = { viewModel.removeStudent(student) },
                                enabled = !uiState.loading
                            ) {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = stringResource(Res.string.button_delete)
                                )
                            }
                        }
                    }

                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall)) {
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
