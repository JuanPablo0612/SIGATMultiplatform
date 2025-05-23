package com.juanpablo0612.sigat.ui.auth.login

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.juanpablo0612.sigat.data.exceptions.InvalidCredentialsException
import com.juanpablo0612.sigat.ui.auth.components.EmailTextField
import com.juanpablo0612.sigat.ui.auth.components.PasswordTextField
import com.juanpablo0612.sigat.ui.components.ErrorCard
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.invalid_credentials_message
import sigat.composeapp.generated.resources.invalid_credentials_title
import sigat.composeapp.generated.resources.login_button
import sigat.composeapp.generated.resources.login_message
import sigat.composeapp.generated.resources.login_title
import sigat.composeapp.generated.resources.unknown_error_message

@Composable
fun DesktopLoginContent(viewModel: LoginViewModel = koinInject(), modifier: Modifier = Modifier) {
    val uiState = viewModel.uiState

//    LaunchedEffect(key1 = uiState.success) {
//        if (uiState.success) {
//            onNavigateToHome()
//        }
//    }


    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(Res.string.login_title),
            style = MaterialTheme.typography.headlineLarge.copy(color = Color.White)
        )

        Text(
            text = stringResource(Res.string.login_message),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        uiState.exception?.let {
            when (it) {
                is InvalidCredentialsException -> {
                    ErrorCard(
                        message = stringResource(Res.string.invalid_credentials_message),
                        title = stringResource(Res.string.invalid_credentials_title),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
                }

                else -> {
                    ErrorCard(
                        message = stringResource(Res.string.unknown_error_message),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
                }
            }
        }

        EmailTextField(
            value = uiState.email,
            onValueChange = viewModel::onEmailChange,
            isError = !uiState.isValidEmail,
            enabled = !uiState.loading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

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
            onClick = viewModel::onLogin,
            enabled = !uiState.loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            AnimatedContent(uiState.loading, label = "Show loading") { loading ->
                if (loading) {
                    CircularProgressIndicator()
                } else {
                    Text(text = stringResource(Res.string.login_button))
                }
            }
        }
    }
}