package com.juanpablo0612.sigat.ui.auth.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    imeAction: ImeAction = ImeAction.Next
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
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
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = imeAction
        ),
        singleLine = true
    )
}

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    showPassword: Boolean,
    onVisibilityChange: () -> Unit,
    isError: Boolean = false,
    imeAction: ImeAction = ImeAction.Done
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
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
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        )
    )
}

@Composable
fun ConfirmPasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    visiblePassword: Boolean,
    isError: Boolean = false,
    imeAction: ImeAction = ImeAction.Done
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
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
        visualTransformation = if (visiblePassword) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        )
    )
}

@Composable
fun IdNumberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    imeAction: ImeAction = ImeAction.Next
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
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
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = imeAction
        ),
        singleLine = true
    )
}

@Composable
fun IdIssuingLocationTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    imeAction: ImeAction = ImeAction.Next
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
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
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = imeAction
        ),
        singleLine = true
    )
}

@Composable
fun FirstNameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    imeAction: ImeAction = ImeAction.Next
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
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
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = imeAction
        ),
        singleLine = true
    )
}

@Composable
fun LastNameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    imeAction: ImeAction = ImeAction.Next
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
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
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = imeAction
        ),
        singleLine = true
    )
}