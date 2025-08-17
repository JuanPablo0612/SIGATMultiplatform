package com.juanpablo0612.sigat.ui.auth.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.juanpablo0612.sigat.ui.utils.DigitOnlyInputTransformation
import com.juanpablo0612.sigat.ui.utils.LetterAndSpaceOnlyInputTransformation
import com.juanpablo0612.sigat.ui.utils.NoSpacesInputTransformation
import org.jetbrains.compose.resources.stringResource
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.confirm_password_error
import sigat.composeapp.generated.resources.confirm_password_label
import sigat.composeapp.generated.resources.email_error
import sigat.composeapp.generated.resources.email_label
import sigat.composeapp.generated.resources.first_name_label
import sigat.composeapp.generated.resources.id_issuing_location_error
import sigat.composeapp.generated.resources.id_issuing_location_label
import sigat.composeapp.generated.resources.id_number_error
import sigat.composeapp.generated.resources.id_number_label
import sigat.composeapp.generated.resources.last_name_error
import sigat.composeapp.generated.resources.last_name_label
import sigat.composeapp.generated.resources.password_error
import sigat.composeapp.generated.resources.password_label

@Composable
fun EmailTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    imeAction: ImeAction = ImeAction.Next
) {
    OutlinedTextField(
        state = state,
        modifier = modifier,
        enabled = enabled,
        label = {
            Text(
                text = stringResource(Res.string.email_label)
            )
        },
        supportingText = if (!isError) null else {
            {
                Text(text = stringResource(Res.string.email_error))
            }
        },
        isError = isError,
        inputTransformation = NoSpacesInputTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = imeAction
        ),
        lineLimits = TextFieldLineLimits.SingleLine
    )
}

@Composable
fun PasswordTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    showPassword: Boolean,
    onVisibilityChange: () -> Unit,
    isError: Boolean = false,
    imeAction: ImeAction = ImeAction.Done
) {
    OutlinedSecureTextField(
        state = state,
        modifier = modifier,
        enabled = enabled,
        label = {
            Text(
                text = stringResource(Res.string.password_label)
            )
        },
        trailingIcon = {
            IconButton(onClick = { onVisibilityChange() }) {
                AnimatedContent(
                    targetState = showPassword,
                    label = "PasswordVisibilityAnimation",
                    transitionSpec = {
                        if (targetState) {
                            slideInVertically { height -> -height }
                                .togetherWith(slideOutVertically { height -> height })
                        } else {
                            slideInVertically { height -> height }
                                .togetherWith(slideOutVertically { height -> -height })
                        }
                    }
                ) { visiblePassword ->
                    if (visiblePassword) {
                        Icon(
                            imageVector = Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null
                        )
                    }
                }
            }
        },
        supportingText = if (!isError) null else {
            {
                Text(text = stringResource(Res.string.password_error))
            }
        },
        isError = isError,
        inputTransformation = NoSpacesInputTransformation(),
        textObfuscationMode = if (showPassword) TextObfuscationMode.Visible else TextObfuscationMode.RevealLastTyped,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        )
    )
}

@Composable
fun ConfirmPasswordTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    visiblePassword: Boolean,
    isError: Boolean = false,
    imeAction: ImeAction = ImeAction.Done
) {
    OutlinedSecureTextField(
        state = state,
        modifier = modifier,
        enabled = enabled,
        label = {
            Text(
                text = stringResource(Res.string.confirm_password_label)
            )
        },
        supportingText = if (!isError) null else {
            {
                Text(text = stringResource(Res.string.confirm_password_error))
            }
        },
        isError = isError,
        inputTransformation = NoSpacesInputTransformation(),
        textObfuscationMode = if (visiblePassword) TextObfuscationMode.Visible else TextObfuscationMode.RevealLastTyped,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        )
    )
}

@Composable
fun IdNumberTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    imeAction: ImeAction = ImeAction.Next
) {
    OutlinedTextField(
        state = state,
        modifier = modifier,
        enabled = enabled,
        label = {
            Text(
                text = stringResource(Res.string.id_number_label)
            )
        },
        supportingText = if (!isError) null else {
            {
                Text(text = stringResource(Res.string.id_number_error))
            }
        },
        isError = isError,
        inputTransformation = DigitOnlyInputTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = imeAction
        ),
        lineLimits = TextFieldLineLimits.SingleLine
    )
}

@Composable
fun IdIssuingLocationTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    imeAction: ImeAction = ImeAction.Next
) {
    OutlinedTextField(
        state = state,
        modifier = modifier,
        enabled = enabled,
        label = {
            Text(
                text = stringResource(Res.string.id_issuing_location_label)
            )
        },
        supportingText = if (!isError) null else {
            {
                Text(text = stringResource(Res.string.id_issuing_location_error))
            }
        },
        isError = isError,
        inputTransformation = LetterAndSpaceOnlyInputTransformation(),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = imeAction
        ),
        lineLimits = TextFieldLineLimits.SingleLine
    )
}

@Composable
fun FirstNameTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    imeAction: ImeAction = ImeAction.Next
) {
    OutlinedTextField(
        state = state,
        modifier = modifier,
        enabled = enabled,
        label = {
            Text(
                text = stringResource(Res.string.first_name_label)
            )
        },
        supportingText = if (!isError) null else {
            {
                Text(text = stringResource(Res.string.email_error))
            }
        },
        isError = isError,
        inputTransformation = LetterAndSpaceOnlyInputTransformation(),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = imeAction
        ),
        lineLimits = TextFieldLineLimits.SingleLine
    )
}

@Composable
fun LastNameTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    imeAction: ImeAction = ImeAction.Next
) {
    OutlinedTextField(
        state = state,
        modifier = modifier,
        enabled = enabled,
        label = {
            Text(
                text = stringResource(Res.string.last_name_label)
            )
        },
        supportingText = if (!isError) null else {
            {
                Text(text = stringResource(Res.string.last_name_error))
            }
        },
        isError = isError,
        inputTransformation = LetterAndSpaceOnlyInputTransformation(),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = imeAction
        ),
        lineLimits = TextFieldLineLimits.SingleLine
    )
}