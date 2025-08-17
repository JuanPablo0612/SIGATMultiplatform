package com.juanpablo0612.sigat.ui.training_programs.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.juanpablo0612.sigat.ui.components.LoadingContent
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.button_add_student
import sigat.composeapp.generated.resources.button_delete
import sigat.composeapp.generated.resources.button_save
import sigat.composeapp.generated.resources.code_label
import sigat.composeapp.generated.resources.end_date_label
import sigat.composeapp.generated.resources.name_label
import sigat.composeapp.generated.resources.schedule_label
import sigat.composeapp.generated.resources.start_date_label
import sigat.composeapp.generated.resources.student_id_label
import sigat.composeapp.generated.resources.training_program_detail_title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingProgramDetailScreen(
    programId: String,
    viewModel: TrainingProgramDetailViewModel = koinViewModel(),
    windowSize: WindowSizeClass,
    onNavigateBack: () -> Unit
) {
    val uiState = viewModel.uiState
    LaunchedEffect(programId) { viewModel.loadTrainingProgram(programId) }
    if (uiState.finished) {
        LaunchedEffect(Unit) { onNavigateBack() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.training_program_detail_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        if (!uiState.loading && uiState.id.isNotEmpty()) {
            Column(
                modifier = Modifier.padding(innerPadding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = uiState.name,
                    onValueChange = viewModel::onNameChange,
                    label = { Text(stringResource(Res.string.name_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !uiState.validName
                )
                TextField(
                    value = uiState.code,
                    onValueChange = viewModel::onCodeChange,
                    label = { Text(stringResource(Res.string.code_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !uiState.validCode
                )
                TextField(
                    value = uiState.startDate,
                    onValueChange = viewModel::onStartDateChange,
                    label = { Text(stringResource(Res.string.start_date_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !uiState.validStartDate
                )
                TextField(
                    value = uiState.endDate,
                    onValueChange = viewModel::onEndDateChange,
                    label = { Text(stringResource(Res.string.end_date_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !uiState.validEndDate
                )
                TextField(
                    value = uiState.schedule,
                    onValueChange = viewModel::onScheduleChange,
                    label = { Text(stringResource(Res.string.schedule_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !uiState.validSchedule
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextField(
                        value = uiState.newStudentId,
                        onValueChange = viewModel::onStudentIdChange,
                        label = { Text(stringResource(Res.string.student_id_label)) },
                        modifier = Modifier.weight(1f)
                    )
                    Button(onClick = { viewModel.addStudent() }) {
                        Text(stringResource(Res.string.button_add_student))
                    }
                }
                LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(uiState.students) { student ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = student, fontWeight = FontWeight.Bold)
                            IconButton(onClick = { viewModel.removeStudent(student) }) {
                                Icon(Icons.Filled.Delete, contentDescription = null)
                            }
                        }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { viewModel.updateTrainingProgram() }, modifier = Modifier.weight(1f)) {
                        Text(stringResource(Res.string.button_save))
                    }
                    Button(onClick = { viewModel.deleteTrainingProgram() }, modifier = Modifier.weight(1f)) {
                        Text(stringResource(Res.string.button_delete))
                    }
                }
            }
        } else {
            LoadingContent(modifier = Modifier.fillMaxSize().padding(innerPadding))
        }
    }
}

