package com.juanpablo0612.sigat.ui.auth.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.juanpablo0612.sigat.ui.auth.components.ConfirmPasswordTextField
import com.juanpablo0612.sigat.ui.auth.components.EmailTextField
import com.juanpablo0612.sigat.ui.auth.components.FirstNameTextField
import com.juanpablo0612.sigat.ui.auth.components.IdIssuingLocationTextField
import com.juanpablo0612.sigat.ui.auth.components.IdNumberTextField
import com.juanpablo0612.sigat.ui.auth.components.LastNameTextField
import com.juanpablo0612.sigat.ui.auth.components.PasswordTextField
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.already_have_an_account
import sigat.composeapp.generated.resources.register_button_text
import sigat.composeapp.generated.resources.sign_in_here

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = koinViewModel(),
    windowSize: WindowSizeClass,
    onNavigateToHome: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState = viewModel.uiState

    LaunchedEffect(uiState.success) {
        if (uiState.success) onNavigateToHome()
    }

    RegisterScreenContent(
        uiState = uiState,
        onFirstNameChange = viewModel::onFirstNameChange,
        onLastNameChange = viewModel::onLastNameChange,
        onIdNumberChange = viewModel::onIdNumberChange,
        onIdIssuingLocationChange = viewModel::onIdIssuingLocationChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onPasswordVisibilityChange = viewModel::onPasswordVisibilityChange,
        onRegisterClick = viewModel::onRegisterClick,
        onNavigateToLogin = onNavigateBack,
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun RegisterScreenContent(
    uiState: RegisterUiState,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onIdNumberChange: (String) -> Unit,
    onIdIssuingLocationChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onPasswordVisibilityChange: () -> Unit,
    onRegisterClick: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            RegisterTopAppBar(onNavigateBack = onNavigateBack)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FirstNameTextField(
                value = uiState.firstName,
                onValueChange = onFirstNameChange,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.loading,
                isError = !uiState.isValidFirstName
            )

            LastNameTextField(
                value = uiState.lastName,
                onValueChange = onLastNameChange,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.loading,
                isError = !uiState.isValidLastName
            )

            IdNumberTextField(
                value = uiState.idNumber,
                onValueChange = onIdNumberChange,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.loading,
                isError = !uiState.isValidIdNumber
            )

            IdIssuingLocationTextField(
                value = uiState.idIssuingLocation,
                onValueChange = onIdIssuingLocationChange,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.loading,
                isError = !uiState.isValidIdIssuingLocation
            )

            EmailTextField(
                value = uiState.email,
                onValueChange = onEmailChange,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.loading,
                isError = !uiState.isValidEmail
            )

            PasswordTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.loading,
                isError = !uiState.isValidPassword,
                showPassword = uiState.showPassword,
                onVisibilityChange = onPasswordVisibilityChange
            )

            ConfirmPasswordTextField(
                value = uiState.confirmPassword,
                onValueChange = onConfirmPasswordChange,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.loading,
                isError = !uiState.isValidConfirmPassword,
                visiblePassword = uiState.showPassword,
                imeAction = ImeAction.Done
            )

            Button(
                onClick = onRegisterClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.loading
            ) {
                if (uiState.loading) {
                    CircularProgressIndicator()
                } else {
                    Text(text = stringResource(Res.string.register_button_text))
                }
            }

            Text(
                text = buildAnnotatedString {
                    append(stringResource(Res.string.already_have_an_account))
                    append(" ")
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append(stringResource(Res.string.sign_in_here))
                    }
                },
                modifier = Modifier.clickable(onClick = onNavigateToLogin),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}