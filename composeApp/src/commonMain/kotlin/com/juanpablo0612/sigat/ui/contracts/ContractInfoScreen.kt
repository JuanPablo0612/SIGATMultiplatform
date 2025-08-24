package com.juanpablo0612.sigat.ui.contracts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.juanpablo0612.sigat.ui.components.LoadingContent
import com.juanpablo0612.sigat.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.button_save
import sigat.composeapp.generated.resources.city_error
import sigat.composeapp.generated.resources.city_label
import sigat.composeapp.generated.resources.contract_info_title
import sigat.composeapp.generated.resources.contract_number_error
import sigat.composeapp.generated.resources.contract_number_label
import sigat.composeapp.generated.resources.contract_object_error
import sigat.composeapp.generated.resources.contract_object_label
import sigat.composeapp.generated.resources.contract_value_error
import sigat.composeapp.generated.resources.contract_value_label
import sigat.composeapp.generated.resources.contract_year_error
import sigat.composeapp.generated.resources.contract_year_label
import sigat.composeapp.generated.resources.contractor_name_error
import sigat.composeapp.generated.resources.contractor_name_label
import sigat.composeapp.generated.resources.elaboration_date_error
import sigat.composeapp.generated.resources.elaboration_date_label
import sigat.composeapp.generated.resources.end_date_error
import sigat.composeapp.generated.resources.end_date_label
import sigat.composeapp.generated.resources.id_issuing_location_error
import sigat.composeapp.generated.resources.id_issuing_location_label
import sigat.composeapp.generated.resources.id_number_error
import sigat.composeapp.generated.resources.id_number_label
import sigat.composeapp.generated.resources.payment_method_error
import sigat.composeapp.generated.resources.payment_method_label
import sigat.composeapp.generated.resources.supervisor_dependency_error
import sigat.composeapp.generated.resources.supervisor_dependency_label
import sigat.composeapp.generated.resources.supervisor_name_error
import sigat.composeapp.generated.resources.supervisor_name_label
import sigat.composeapp.generated.resources.supervisor_position_error
import sigat.composeapp.generated.resources.supervisor_position_label

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContractInfoScreen(
    viewModel: ContractInfoViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    windowSize: WindowSizeClass,
) {
    val uiState = viewModel.uiState

    if (uiState.saved) {
        LaunchedEffect(Unit) { onNavigateBack() }
    }

    val contentModifier = if (windowSize.widthSizeClass > WindowWidthSizeClass.Compact) {
        Modifier.widthIn(max = 600.dp)
    } else {
        Modifier.fillMaxWidth()
    }

    Scaffold(topBar = { TopAppBar(title = { Text(stringResource(Res.string.contract_info_title)) }) }) { innerPadding ->
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
                OutlinedTextField(
                    value = uiState.city,
                    onValueChange = viewModel::onCityChange,
                    label = { Text(stringResource(Res.string.city_label)) },
                    isError = !uiState.isValidCity,
                    supportingText = if (uiState.isValidCity) null else {
                        { Text(text = stringResource(Res.string.city_error)) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = uiState.supervisorName,
                    onValueChange = viewModel::onSupervisorNameChange,
                    label = { Text(stringResource(Res.string.supervisor_name_label)) },
                    isError = !uiState.isValidSupervisorName,
                    supportingText = if (uiState.isValidSupervisorName) null else {
                        { Text(text = stringResource(Res.string.supervisor_name_error)) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = uiState.supervisorPosition,
                    onValueChange = viewModel::onSupervisorPositionChange,
                    label = { Text(stringResource(Res.string.supervisor_position_label)) },
                    isError = !uiState.isValidSupervisorPosition,
                    supportingText = if (uiState.isValidSupervisorPosition) null else {
                        { Text(text = stringResource(Res.string.supervisor_position_error)) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = uiState.supervisorDependency,
                    onValueChange = viewModel::onSupervisorDependencyChange,
                    label = { Text(stringResource(Res.string.supervisor_dependency_label)) },
                    isError = !uiState.isValidSupervisorDependency,
                    supportingText = if (uiState.isValidSupervisorDependency) null else {
                        { Text(text = stringResource(Res.string.supervisor_dependency_error)) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = uiState.number,
                    onValueChange = viewModel::onContractNumberChange,
                    label = { Text(stringResource(Res.string.contract_number_label)) },
                    isError = !uiState.isValidNumber,
                    supportingText = if (uiState.isValidNumber) null else {
                        { Text(text = stringResource(Res.string.contract_number_error)) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = uiState.year,
                    onValueChange = viewModel::onContractYearChange,
                    label = { Text(stringResource(Res.string.contract_year_label)) },
                    isError = !uiState.isValidYear,
                    supportingText = if (uiState.isValidYear) null else {
                        { Text(text = stringResource(Res.string.contract_year_error)) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = uiState.contractorName,
                    onValueChange = viewModel::onContractorNameChange,
                    label = { Text(stringResource(Res.string.contractor_name_label)) },
                    isError = !uiState.isValidContractorName,
                    supportingText = if (uiState.isValidContractorName) null else {
                        { Text(text = stringResource(Res.string.contractor_name_error)) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = uiState.contractorIdNumber,
                    onValueChange = viewModel::onContractorIdNumberChange,
                    label = { Text(stringResource(Res.string.id_number_label)) },
                    isError = !uiState.isValidContractorIdNumber,
                    supportingText = if (uiState.isValidContractorIdNumber) null else {
                        { Text(text = stringResource(Res.string.id_number_error)) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = uiState.contractorIdExpeditionPlace,
                    onValueChange = viewModel::onContractorIdExpeditionChange,
                    label = { Text(stringResource(Res.string.id_issuing_location_label)) },
                    isError = !uiState.isValidContractorIdExpedition,
                    supportingText = if (uiState.isValidContractorIdExpedition) null else {
                        { Text(text = stringResource(Res.string.id_issuing_location_error)) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = uiState.contractObject,
                    onValueChange = viewModel::onContractObjectChange,
                    label = { Text(stringResource(Res.string.contract_object_label)) },
                    isError = !uiState.isValidContractObject,
                    supportingText = if (uiState.isValidContractObject) null else {
                        { Text(text = stringResource(Res.string.contract_object_error)) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = uiState.value,
                    onValueChange = viewModel::onContractValueChange,
                    label = { Text(stringResource(Res.string.contract_value_label)) },
                    isError = !uiState.isValidContractValue,
                    supportingText = if (uiState.isValidContractValue) null else {
                        { Text(text = stringResource(Res.string.contract_value_error)) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = uiState.paymentMethod,
                    onValueChange = viewModel::onPaymentMethodChange,
                    label = { Text(stringResource(Res.string.payment_method_label)) },
                    isError = !uiState.isValidPaymentMethod,
                    supportingText = if (uiState.isValidPaymentMethod) null else {
                        { Text(text = stringResource(Res.string.payment_method_error)) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = uiState.elaborationDate,
                    onValueChange = viewModel::onElaborationDateChange,
                    label = { Text(stringResource(Res.string.elaboration_date_label)) },
                    isError = !uiState.isValidElaborationDate,
                    supportingText = if (uiState.isValidElaborationDate) null else {
                        { Text(text = stringResource(Res.string.elaboration_date_error)) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = uiState.endDate,
                    onValueChange = viewModel::onEndDateChange,
                    label = { Text(stringResource(Res.string.end_date_label)) },
                    isError = !uiState.isValidEndDate,
                    supportingText = if (uiState.isValidEndDate) null else {
                        { Text(text = stringResource(Res.string.end_date_error)) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                Button(
                    onClick = viewModel::onSave,
                    enabled = !uiState.loading
                ) {
                    Text(text = stringResource(Res.string.button_save))
                }
            }
            if (uiState.loading) {
                LoadingContent(modifier = Modifier.matchParentSize())
            }
        }
    }
}
