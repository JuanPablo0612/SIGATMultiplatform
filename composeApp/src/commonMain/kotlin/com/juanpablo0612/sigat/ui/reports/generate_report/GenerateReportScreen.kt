package com.juanpablo0612.sigat.ui.reports.generate_report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Input
import androidx.compose.material.icons.filled.Output
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.juanpablo0612.sigat.ui.contracts.ContractFields
import com.juanpablo0612.sigat.ui.theme.Dimens
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import io.github.vinceglb.filekit.name
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.default_report_file_name
import sigat.composeapp.generated.resources.generate_report_button
import sigat.composeapp.generated.resources.generate_report_title
import sigat.composeapp.generated.resources.select_output_file
import sigat.composeapp.generated.resources.select_template

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateReportScreen(
    viewModel: GenerateReportViewModel = koinViewModel(),
    windowSize: WindowSizeClass
) {
    val uiState = viewModel.uiState

    val templateFileLauncher = rememberFilePickerLauncher(type = FileKitType.File("doc", "docx")) { file ->
        file?.let {
            viewModel.onTemplateFileSelected(it)
        }
    }
    val outputFileLauncher = rememberFileSaverLauncher { file ->
        file?.let {
            viewModel.onOutputFileSelected(it)
        }
    }

    val contentModifier = if (windowSize.widthSizeClass > WindowWidthSizeClass.Compact) {
        Modifier.widthIn(max = 600.dp)
    } else {
        Modifier.fillMaxWidth()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(Res.string.generate_report_title)) }) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = contentModifier
                    .padding(horizontal = Dimens.PaddingMedium)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall)
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
                selectedFileName = uiState.templateFile?.name,
                onClick = { templateFileLauncher.launch() },
                modifier = Modifier.fillMaxWidth()
            )
            val defaultFileName = stringResource(Res.string.default_report_file_name)
            OutputFileSelector(
                selectedFileName = uiState.outputFile?.name,
                onClick = { outputFileLauncher.launch(defaultFileName, "docx") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = viewModel::onGenerateReportClick) {
                Text(text = stringResource(Res.string.generate_report_button))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateSelector(selectedFileName: String?, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val displayText = selectedFileName ?: stringResource(Res.string.select_template)
    OutlinedTextField(
        value = displayText,
        onValueChange = {},
        readOnly = true,
        label = { Text(stringResource(Res.string.select_template)) },
        trailingIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.Input,
                    contentDescription = stringResource(Res.string.select_template)
                )
            }
        },
        modifier = modifier.fillMaxWidth(),
        singleLine = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutputFileSelector(selectedFileName: String?, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val displayText = selectedFileName ?: stringResource(Res.string.select_output_file)
    OutlinedTextField(
        value = displayText,
        onValueChange = {},
        readOnly = true,
        label = { Text(stringResource(Res.string.select_output_file)) },
        trailingIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Default.Output,
                    contentDescription = stringResource(Res.string.select_output_file)
                )
            }
        },
        modifier = modifier.fillMaxWidth(),
        singleLine = true
    )
}