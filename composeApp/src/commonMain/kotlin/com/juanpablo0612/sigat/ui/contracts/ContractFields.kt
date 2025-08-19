package com.juanpablo0612.sigat.ui.contracts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.juanpablo0612.sigat.domain.model.Contract
import org.jetbrains.compose.resources.stringResource
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.city_label
import sigat.composeapp.generated.resources.contract_number_label
import sigat.composeapp.generated.resources.contract_object_label
import sigat.composeapp.generated.resources.contract_value_label
import sigat.composeapp.generated.resources.contract_year_label
import sigat.composeapp.generated.resources.contractor_name_label
import sigat.composeapp.generated.resources.elaboration_date_label
import sigat.composeapp.generated.resources.end_date_label
import sigat.composeapp.generated.resources.id_issuing_location_label
import sigat.composeapp.generated.resources.id_number_label
import sigat.composeapp.generated.resources.payment_method_label
import sigat.composeapp.generated.resources.supervisor_dependency_label
import sigat.composeapp.generated.resources.supervisor_name_label
import sigat.composeapp.generated.resources.supervisor_position_label

/**
 * Collection of input fields for contract information.
 */
@Composable
fun ContractFields(
    contract: Contract,
    onCityChange: (String) -> Unit,
    onSupervisorNameChange: (String) -> Unit,
    onSupervisorPositionChange: (String) -> Unit,
    onSupervisorDependencyChange: (String) -> Unit,
    onContractNumberChange: (String) -> Unit,
    onContractYearChange: (String) -> Unit,
    onContractorNameChange: (String) -> Unit,
    onContractorIdNumberChange: (String) -> Unit,
    onContractorIdExpeditionChange: (String) -> Unit,
    onContractObjectChange: (String) -> Unit,
    onContractValueChange: (String) -> Unit,
    onPaymentMethodChange: (String) -> Unit,
    onElaborationDateChange: (String) -> Unit,
    onEndDateChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        OutlinedTextField(
            value = contract.city,
            onValueChange = onCityChange,
            label = { Text(stringResource(Res.string.city_label)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = contract.supervisorName,
            onValueChange = onSupervisorNameChange,
            label = { Text(stringResource(Res.string.supervisor_name_label)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = contract.supervisorPosition,
            onValueChange = onSupervisorPositionChange,
            label = { Text(stringResource(Res.string.supervisor_position_label)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = contract.supervisorDependency,
            onValueChange = onSupervisorDependencyChange,
            label = { Text(stringResource(Res.string.supervisor_dependency_label)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = contract.number,
            onValueChange = onContractNumberChange,
            label = { Text(stringResource(Res.string.contract_number_label)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = contract.year,
            onValueChange = onContractYearChange,
            label = { Text(stringResource(Res.string.contract_year_label)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = contract.contractorName,
            onValueChange = onContractorNameChange,
            label = { Text(stringResource(Res.string.contractor_name_label)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = contract.contractorIdNumber,
            onValueChange = onContractorIdNumberChange,
            label = { Text(stringResource(Res.string.id_number_label)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = contract.contractorIdExpeditionPlace,
            onValueChange = onContractorIdExpeditionChange,
            label = { Text(stringResource(Res.string.id_issuing_location_label)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = contract.contractObject,
            onValueChange = onContractObjectChange,
            label = { Text(stringResource(Res.string.contract_object_label)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = contract.value,
            onValueChange = onContractValueChange,
            label = { Text(stringResource(Res.string.contract_value_label)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = contract.paymentMethod,
            onValueChange = onPaymentMethodChange,
            label = { Text(stringResource(Res.string.payment_method_label)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = contract.elaborationDate,
            onValueChange = onElaborationDateChange,
            label = { Text(stringResource(Res.string.elaboration_date_label)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = contract.endDate,
            onValueChange = onEndDateChange,
            label = { Text(stringResource(Res.string.end_date_label)) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

