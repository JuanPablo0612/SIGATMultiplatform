package com.juanpablo0612.sigat.ui.auth.lregister

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.juanpablo0612.sigat.data.exceptions.DniIsNotRegisteredException
import com.juanpablo0612.sigat.data.exceptions.DniLinkedToAccountException
import com.juanpablo0612.sigat.data.exceptions.UserAlreadyExistsException
import com.juanpablo0612.sigat.ui.auth.components.DniTextField
import com.juanpablo0612.sigat.ui.auth.components.EmailTextField
import com.juanpablo0612.sigat.ui.auth.components.FirstNameTextField
import com.juanpablo0612.sigat.ui.auth.components.LastNameTextField
import com.juanpablo0612.sigat.ui.auth.components.PasswordTextField
import com.juanpablo0612.sigat.ui.auth.register.RegisterViewModel
import com.juanpablo0612.sigat.ui.components.ErrorCard
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.dni_already_linked_message
import sigat.composeapp.generated.resources.dni_already_linked_title
import sigat.composeapp.generated.resources.dni_not_registered_message
import sigat.composeapp.generated.resources.dni_not_registered_title
import sigat.composeapp.generated.resources.register_title
import sigat.composeapp.generated.resources.user_already_exists_message
import sigat.composeapp.generated.resources.user_already_exists_title

@Composable
fun DesktopRegisterContent(
    viewModel: RegisterViewModel = koinInject(),
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState

    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(Res.string.register_title),
            style = MaterialTheme.typography.headlineLarge.copy(color = Color.White),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        DniTextField(
            value = uiState.dni,
            onValueChange = viewModel::onDniChange,
            isError = !uiState.isValidDni,
            enabled = !uiState.loading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        if (uiState.exception is DniIsNotRegisteredException) {
            ErrorCard(
                title = stringResource(Res.string.dni_not_registered_title),
                message = stringResource(Res.string.dni_not_registered_message),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }

        if (uiState.exception is DniLinkedToAccountException) {
            ErrorCard(
                title = stringResource(Res.string.dni_already_linked_title),
                message = stringResource(Res.string.dni_already_linked_message),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }

        FirstNameTextField(
            value = uiState.firstName,
            onValueChange = viewModel::onFirstNameChange,
            isError = !uiState.isValidFirstName,
            enabled = !uiState.loading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        LastNameTextField(
            value = uiState.lastName,
            onValueChange = viewModel::onLastNameChange,
            isError = !uiState.isValidLastName,
            enabled = !uiState.loading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        EmailTextField(
            value = uiState.email,
            onValueChange = viewModel::onEmailChange,
            isError = !uiState.isValidEmail,
            enabled = !uiState.loading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        if (uiState.exception is UserAlreadyExistsException) {
            ErrorCard(
                title = stringResource(Res.string.user_already_exists_title),
                message = stringResource(Res.string.user_already_exists_message),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }

        PasswordTextField(
            value = uiState.password,
            onValueChange = viewModel::onPasswordChange,
            visiblePassword = uiState.isPasswordVisible,
            onVisibilityChange = viewModel::onPasswordVisibilityChange,
            isError = !uiState.isValidPassword,
            enabled = !uiState.loading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = viewModel::onRegister,
            enabled = !uiState.loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.loading) {
                CircularProgressIndicator()
            } else {
                Text(text = stringResource(Res.string.register_title))
            }
        }
    }
}