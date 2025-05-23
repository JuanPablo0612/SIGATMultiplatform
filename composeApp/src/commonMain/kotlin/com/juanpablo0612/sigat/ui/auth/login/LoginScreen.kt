package com.juanpablo0612.sigat.ui.auth.login

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.juanpablo0612.sigat.data.exceptions.InvalidCredentialsException
import com.juanpablo0612.sigat.ui.auth.components.EmailTextField
import com.juanpablo0612.sigat.ui.auth.components.PasswordTextField
import com.juanpablo0612.sigat.ui.components.ErrorCard
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.auth_bg
import sigat.composeapp.generated.resources.create_account_button
import sigat.composeapp.generated.resources.invalid_credentials_message
import sigat.composeapp.generated.resources.invalid_credentials_title
import sigat.composeapp.generated.resources.login_button
import sigat.composeapp.generated.resources.login_message
import sigat.composeapp.generated.resources.login_title
import sigat.composeapp.generated.resources.unknown_error_message

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinInject(),
    windowSize: WindowSizeClass,
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val uiState = viewModel.uiState
    val fieldsSizeModifier = remember(windowSize.widthSizeClass) {
        if (windowSize.widthSizeClass > WindowWidthSizeClass.Compact) {
            Modifier.fillMaxWidth(0.8f)
        } else Modifier.fillMaxWidth()
    }

    val loginContentModifier = remember(windowSize.widthSizeClass) {
        if (windowSize.widthSizeClass > WindowWidthSizeClass.Compact) {
            Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight()
        } else Modifier.fillMaxSize().padding(16.dp)
    }

    LaunchedEffect(key1 = uiState.success) {
        if (uiState.success) {
            onNavigateToHome()
        }
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (windowSize.widthSizeClass > WindowWidthSizeClass.Compact) {
                Image(
                    painter = painterResource(Res.drawable.auth_bg),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = loginContentModifier
                    .verticalScroll(rememberScrollState())
                    .background(color = MaterialTheme.colorScheme.background)
                    .clip(MaterialTheme.shapes.medium)
                    .align(Alignment.CenterEnd)
            ) {
                Text(
                    text = stringResource(Res.string.login_title),
                    style = MaterialTheme.typography.headlineLarge.copy(/*fontWeight = FontWeight.Bold*/)
                )

                Text(
                    text = stringResource(Res.string.login_message),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                uiState.exception?.let {
                    when (it) {
                        is InvalidCredentialsException -> {
                            ErrorCard(
                                message = stringResource(Res.string.invalid_credentials_message),
                                title = stringResource(Res.string.invalid_credentials_title),
                                modifier = fieldsSizeModifier
                                    .padding(bottom = 16.dp)
                            )
                        }

                        else -> {
                            ErrorCard(
                                message = stringResource(Res.string.unknown_error_message),
                                modifier = fieldsSizeModifier
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
                    modifier = fieldsSizeModifier
                        .padding(bottom = 16.dp)
                )

                PasswordTextField(
                    value = uiState.password,
                    onValueChange = viewModel::onPasswordChange,
                    visiblePassword = uiState.isPasswordVisible,
                    onVisibilityChange = viewModel::onPasswordVisibilityChange,
                    isError = !uiState.isValidPassword,
                    enabled = !uiState.loading,
                    modifier = fieldsSizeModifier
                        .padding(bottom = 16.dp)
                )

                Button(
                    onClick = viewModel::onLogin,
                    enabled = !uiState.loading,
                    modifier = fieldsSizeModifier
                        .padding(bottom = 16.dp)
                ) {
                    AnimatedContent(uiState.loading, label = "Show loading") { loading ->
                        if (loading) {
                            CircularProgressIndicator()
                        } else {
                            Text(text = stringResource(Res.string.login_button))
                        }
                    }
                }

                TextButton(
                    onClick = { onNavigateToRegister() },
                    enabled = !uiState.loading
                ) {
                    Text(text = stringResource(Res.string.create_account_button))
                }
            }
        }
    }
}