package com.juanpablo0612.sigat.ui.training_programs.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.juanpablo0612.sigat.domain.model.TrainingProgram
import com.juanpablo0612.sigat.ui.components.LoadingContent
import com.juanpablo0612.sigat.ui.theme.Dimens
import org.koin.compose.viewmodel.koinViewModel
import org.jetbrains.compose.resources.stringResource
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.add_training_program_title

@Composable
fun TrainingProgramListScreen(
    viewModel: TrainingProgramListViewModel = koinViewModel(),
    windowSize: WindowSizeClass,
    onAddProgramClick: () -> Unit,
    onProgramClick: (String) -> Unit
) {
    val uiState = viewModel.uiState

    Scaffold(
        topBar = { TrainingProgramListTopBar() },
        floatingActionButton = {
            if (!uiState.loading) {
                FloatingActionButton(onClick = onAddProgramClick) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = stringResource(Res.string.add_training_program_title)
                    )
                }
            }
        }
    ) { innerPadding ->
        if (!uiState.loading) {
            LazyColumn(
                modifier = Modifier.padding(innerPadding).fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall)
            ) {
                items(uiState.programs, key = { it.id }) {
                    TrainingProgramItem(program = it, onClick = { onProgramClick(it.id) })
                }
            }
        } else {
            LoadingContent(modifier = Modifier.padding(innerPadding).fillMaxSize())
        }
    }
}

@Composable
private fun TrainingProgramItem(program: TrainingProgram, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens.PaddingMedium).clickable { onClick() }) {
        Column(modifier = Modifier.padding(Dimens.PaddingMedium), verticalArrangement = Arrangement.spacedBy(Dimens.PaddingExtraSmall)) {
            Text(text = program.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Text(text = program.schedule, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
