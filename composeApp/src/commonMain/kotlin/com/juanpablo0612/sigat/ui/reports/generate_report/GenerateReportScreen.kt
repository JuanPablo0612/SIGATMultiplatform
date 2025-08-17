package com.juanpablo0612.sigat.ui.reports.generate_report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Input
import androidx.compose.material.icons.filled.Output
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GenerateReportScreen(viewModel: GenerateReportViewModel = koinViewModel()) {
    val uiState = viewModel.uiState

    val templateFileLauncher = rememberFilePickerLauncher { file ->
        file?.let {
            viewModel.onTemplateFileSelected(it)
        }
    }
    val outputFileLauncher = rememberFileSaverLauncher { file ->
        file?.let {
            viewModel.onOutputFileSelected(it)
        }
    }

    Scaffold { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            TemplateSelector(
                onClick = {
                    templateFileLauncher.launch()
                }
            )
            OutputFileSelector(
                onClick = {
                    outputFileLauncher.launch("informe", "docx")
                }
            )
            Button(onClick = viewModel::onGenerateReportClick) {
                Text(text = "Generar informe")
            }
        }
    }
}

@Composable
fun TemplateSelector(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(modifier = modifier, onClick = onClick) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.Input,
                contentDescription = null
            )
            Text(
                text = "Seleccionar plantilla"
            )
        }
    }
}

@Composable
fun OutputFileSelector(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(modifier = modifier, onClick = onClick) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Output,
                contentDescription = null
            )
            Text(
                text = "Seleccionar archivo de salida"
            )
        }
    }
}