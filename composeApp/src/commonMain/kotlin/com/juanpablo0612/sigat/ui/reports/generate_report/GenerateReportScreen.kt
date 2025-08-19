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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.juanpablo0612.sigat.ui.contracts.ContractFields
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import org.koin.compose.viewmodel.koinViewModel
import org.jetbrains.compose.resources.stringResource
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.button_generate_report
import sigat.composeapp.generated.resources.generate_report_title
import sigat.composeapp.generated.resources.select_output_file
import sigat.composeapp.generated.resources.select_template
import sigat.composeapp.generated.resources.report_default_filename
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(Res.string.generate_report_title)) }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ContractFields(
                contract = uiState.contract,
                onCityChange = viewModel::onCityChange,
                onSupervisorNameChange = viewModel::onSupervisorNameChange,
                onSupervisorPositionChange = viewModel::onSupervisorPositionChange,
                onSupervisorDependencyChange = viewModel::onSupervisorDependencyChange,
                onContractNumberChange = viewModel::onContractNumberChange,
                onContractYearChange = viewModel::onContractYearChange,
                onContractorNameChange = viewModel::onContractorNameChange,
                onContractorIdNumberChange = viewModel::onContractorIdNumberChange,
                onContractorIdExpeditionChange = viewModel::onContractorIdExpeditionChange,
                onContractObjectChange = viewModel::onContractObjectChange,
                onContractValueChange = viewModel::onContractValueChange,
                onPaymentMethodChange = viewModel::onPaymentMethodChange,
                onElaborationDateChange = viewModel::onElaborationDateChange,
                onEndDateChange = viewModel::onEndDateChange,
            )
            TemplateSelector(
                onClick = { templateFileLauncher.launch() }
            )
            val defaultReportFileName = stringResource(Res.string.report_default_filename)
            OutputFileSelector(
                onClick = { outputFileLauncher.launch(defaultReportFileName, "docx") }
            )
            Button(onClick = viewModel::onGenerateReportClick) {
                Text(text = stringResource(Res.string.button_generate_report))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
                text = stringResource(Res.string.select_template)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
                text = stringResource(Res.string.select_output_file)
            )
        }
    }
}