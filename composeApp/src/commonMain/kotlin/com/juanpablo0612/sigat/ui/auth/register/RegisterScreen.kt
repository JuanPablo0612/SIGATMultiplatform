package com.juanpablo0612.sigat.ui.auth.register

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.juanpablo0612.sigat.data.exceptions.DniIsNotRegisteredException
import com.juanpablo0612.sigat.data.exceptions.DniLinkedToAccountException
import com.juanpablo0612.sigat.data.exceptions.UserAlreadyExistsException
import com.juanpablo0612.sigat.ui.auth.components.DniTextField
import com.juanpablo0612.sigat.ui.auth.components.EmailTextField
import com.juanpablo0612.sigat.ui.auth.components.FirstNameTextField
import com.juanpablo0612.sigat.ui.auth.components.LastNameTextField
import com.juanpablo0612.sigat.ui.auth.components.PasswordTextField
import com.juanpablo0612.sigat.ui.components.ErrorCard
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.auth_bg
import sigat.composeapp.generated.resources.dni_already_linked_message
import sigat.composeapp.generated.resources.dni_already_linked_title
import sigat.composeapp.generated.resources.dni_not_registered_message
import sigat.composeapp.generated.resources.dni_not_registered_title
import sigat.composeapp.generated.resources.login_button
import sigat.composeapp.generated.resources.register_title
import sigat.composeapp.generated.resources.user_already_exists_message
import sigat.composeapp.generated.resources.user_already_exists_title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = koinInject(),
    windowSize: WindowSizeClass,
    onNavigateToHome: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState = viewModel.uiState
    val fieldsSizeModifier = remember(windowSize.widthSizeClass) {
        if (windowSize.widthSizeClass > WindowWidthSizeClass.Compact) {
            Modifier.fillMaxWidth(0.8f)
        } else Modifier.fillMaxWidth()
    }

    val registerContentModifier = remember(windowSize.widthSizeClass) {
        if (windowSize.widthSizeClass > WindowWidthSizeClass.Compact) {
            Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight()
        } else Modifier.fillMaxSize().padding(16.dp)
    }

    LaunchedEffect(key1 = uiState.success) {
        if (uiState.success) onNavigateToHome()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { innerPadding ->
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
                modifier = registerContentModifier
                    .verticalScroll(rememberScrollState())
                    .background(color = MaterialTheme.colorScheme.background)
                    .clip(MaterialTheme.shapes.medium)
                    .align(Alignment.CenterStart)
            ) {
                Text(
                    text = stringResource(Res.string.register_title),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                DniTextField(
                    value = uiState.dni,
                    onValueChange = viewModel::onDniChange,
                    isError = !uiState.isValidDni,
                    enabled = !uiState.loading,
                    modifier = fieldsSizeModifier
                        .padding(bottom = 16.dp)
                )

                if (uiState.exception is DniIsNotRegisteredException) {
                    ErrorCard(
                        title = stringResource(Res.string.dni_not_registered_title),
                        message = stringResource(Res.string.dni_not_registered_message),
                        modifier = fieldsSizeModifier
                            .padding(bottom = 16.dp)
                    )
                }

                if (uiState.exception is DniLinkedToAccountException) {
                    ErrorCard(
                        title = stringResource(Res.string.dni_already_linked_title),
                        message = stringResource(Res.string.dni_already_linked_message),
                        modifier = fieldsSizeModifier
                            .padding(bottom = 16.dp)
                    )
                }

                FirstNameTextField(
                    value = uiState.firstName,
                    onValueChange = viewModel::onFirstNameChange,
                    isError = !uiState.isValidFirstName,
                    enabled = !uiState.loading,
                    modifier = fieldsSizeModifier
                        .padding(bottom = 16.dp)
                )

                LastNameTextField(
                    value = uiState.lastName,
                    onValueChange = viewModel::onLastNameChange,
                    isError = !uiState.isValidLastName,
                    enabled = !uiState.loading,
                    modifier = fieldsSizeModifier
                        .padding(bottom = 16.dp)
                )

                EmailTextField(
                    value = uiState.email,
                    onValueChange = viewModel::onEmailChange,
                    isError = !uiState.isValidEmail,
                    enabled = !uiState.loading,
                    modifier = fieldsSizeModifier
                        .padding(bottom = 16.dp)
                )

                if (uiState.exception is UserAlreadyExistsException) {
                    ErrorCard(
                        title = stringResource(Res.string.user_already_exists_title),
                        message = stringResource(Res.string.user_already_exists_message),
                        modifier = fieldsSizeModifier
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
                    modifier = fieldsSizeModifier
                        .padding(bottom = 16.dp)
                )

                Button(
                    onClick = viewModel::onRegister,
                    enabled = !uiState.loading,
                    modifier = fieldsSizeModifier.padding(bottom = 16.dp)
                ) {
                    if (uiState.loading) {
                        CircularProgressIndicator()
                    } else {
                        Text(text = stringResource(Res.string.register_title))
                    }
                }

                TextButton(
                    onClick = onNavigateBack,
                    enabled = !uiState.loading
                ) {
                    Text(text = stringResource(Res.string.login_button))
                }
            }
        }
    }
}