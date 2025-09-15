package com.juanpablo0612.sigat.ui.auth.login

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.juanpablo0612.sigat.ui.theme.LocalSpacing
import com.juanpablo0612.sigat.ui.auth.components.EmailTextField
import com.juanpablo0612.sigat.ui.auth.components.PasswordTextField
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.create_one_here
import sigat.composeapp.generated.resources.dont_have_an_account
import sigat.composeapp.generated.resources.login_button
import sigat.composeapp.generated.resources.login_message
import sigat.composeapp.generated.resources.login_title

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    windowSize: WindowSizeClass,
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val uiState = viewModel.uiState

    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            onNavigateToHome()
        }
    }

    LoginScreenContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onPasswordVisibilityChange = viewModel::onPasswordVisibilityChange,
        onLoginClick = viewModel::onLoginClick,
        onNavigateToRegister = onNavigateToRegister,
        windowSize = windowSize
    )
}

@Composable
fun LoginScreenContent(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityChange: () -> Unit,
    onLoginClick: () -> Unit,
    onNavigateToRegister: () -> Unit,
    windowSize: WindowSizeClass
) {
    val spacing = LocalSpacing.current
    val columnWidthModifier = Modifier
        .fillMaxWidth()
        .then(
            if (windowSize.widthSizeClass > WindowWidthSizeClass.Compact) {
                Modifier.widthIn(max = 400.dp)
            } else {
                Modifier
            }
        )
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = columnWidthModifier
                    .padding(horizontal = spacing.medium)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(
                    spacing.medium,
                    Alignment.CenterVertically
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(Res.string.login_title),
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                )

                Text(
                    text = stringResource(Res.string.login_message),
                    style = MaterialTheme.typography.bodyLarge
                )

                EmailTextField(
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    modifier = Modifier.fillMaxWidth(),
                    isError = !uiState.isValidEmail
                )

                PasswordTextField(
                    value = uiState.password,
                    onValueChange = onPasswordChange,
                    showPassword = uiState.showPassword,
                    onVisibilityChange = onPasswordVisibilityChange,
                    modifier = Modifier.fillMaxWidth(),
                    isError = !uiState.isValidPassword
                )

                Button(
                    onClick = onLoginClick,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.loading
                ) {
                    if (uiState.loading) {
                        CircularProgressIndicator()
                    } else {
                        Text(text = stringResource(Res.string.login_button))
                    }
                }

                Text(
                    text = buildAnnotatedString {
                        append(stringResource(Res.string.dont_have_an_account))
                        append(" ")
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                            append(stringResource(Res.string.create_one_here))
                        }
                    },
                    modifier = Modifier.clickable(onClick = onNavigateToRegister),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}