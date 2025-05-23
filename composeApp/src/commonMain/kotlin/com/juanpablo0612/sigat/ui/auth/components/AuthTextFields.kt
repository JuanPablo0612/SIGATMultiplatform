package com.juanpablo0612.sigat.ui.auth.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.dni_error
import sigat.composeapp.generated.resources.dni_label
import sigat.composeapp.generated.resources.dni_placeholder
import sigat.composeapp.generated.resources.email_error
import sigat.composeapp.generated.resources.email_label
import sigat.composeapp.generated.resources.email_placeholder
import sigat.composeapp.generated.resources.first_name_label
import sigat.composeapp.generated.resources.first_name_placeholder
import sigat.composeapp.generated.resources.forgot_password
import sigat.composeapp.generated.resources.last_name_error
import sigat.composeapp.generated.resources.last_name_label
import sigat.composeapp.generated.resources.last_name_placeholder
import sigat.composeapp.generated.resources.password_error
import sigat.composeapp.generated.resources.password_label
import sigat.composeapp.generated.resources.password_placeholder

@Composable
private fun BaseTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(
        modifier = modifier
    ) {
        label?.let {
            label()
            Spacer(modifier = Modifier.height(8.dp))
        }

        TextField(
            value = value,
            onValueChange = onValueChange,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            placeholder = placeholder,
            keyboardOptions = keyboardOptions,
            shape = MaterialTheme.shapes.extraLarge,
            colors = TextFieldDefaults.colors(
                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                errorLeadingIconColor = MaterialTheme.colorScheme.error,
                disabledIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            isError = isError,
            singleLine = true,
            supportingText = supportingText,
            visualTransformation = visualTransformation,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun TextFieldLabel(text: String, isError: Boolean, modifier: Modifier = Modifier) {
    val textStyle =
        if (isError) MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.error) else MaterialTheme.typography.bodyMedium.copy(color = Color.White)

    Text(
        text = text,
        style = textStyle,
        modifier = modifier
    )
}

@Composable
fun EmailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false
) {
    BaseTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        placeholder = {
            Text(text = stringResource(Res.string.email_placeholder))
        },
        label = {
            TextFieldLabel(
                text = stringResource(Res.string.email_label),
                isError = isError
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Email,
                contentDescription = null
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        isError = isError,
        supportingText = if (!isError) null else {
            {
                Text(text = stringResource(Res.string.email_error))
            }
        },
    )
}

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onForgotPasswordClick: (() -> Unit)? = null,
    visiblePassword: Boolean,
    onVisibilityChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
) {
    BaseTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        placeholder = {
            Text(text = stringResource(Res.string.password_placeholder))
        },
        label = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextFieldLabel(
                    text = stringResource(Res.string.password_label),
                    isError = isError
                )

                onForgotPasswordClick?.let {
                    Text(
                        text = stringResource(Res.string.forgot_password),
                        style = MaterialTheme.typography.bodyMedium.copy(color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary),
                        modifier = Modifier.clickable { it() }
                    )
                }
            }
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = null
            )
        },
        trailingIcon = {
            IconButton(onClick = { onVisibilityChange(!visiblePassword) }) {
                AnimatedContent(
                    targetState = visiblePassword,
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
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        isError = isError,
        visualTransformation = if (!visiblePassword) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        supportingText = if (!isError) null else {
            {
                Text(text = stringResource(Res.string.password_error))
            }
        }
    )
}

@Composable
fun DniTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false
) {
    BaseTextField(
        value = value,
        onValueChange = {
            if (it.isBlank()) onValueChange(it)
            else {
                if (it.last().isDigit()) {
                    onValueChange(it)
                }
            }
        },
        modifier = modifier,
        enabled = enabled,
        placeholder = {
            Text(text = stringResource(Res.string.dni_placeholder))
        },
        label = {
            TextFieldLabel(
                text = stringResource(Res.string.dni_label),
                isError = isError
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Numbers,
                contentDescription = null
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        isError = isError,
        supportingText = if (!isError) null else {
            {
                Text(text = stringResource(Res.string.dni_error))
            }
        },
    )
}

@Composable
fun FirstNameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false
) {
    BaseTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        placeholder = {
            Text(text = stringResource(Res.string.first_name_placeholder))
        },
        label = {
            TextFieldLabel(
                text = stringResource(Res.string.first_name_label),
                isError = isError
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = null
            )
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next
        ),
        isError = isError,
        supportingText = if (!isError) null else {
            {
                Text(text = stringResource(Res.string.email_error))
            }
        },
    )
}

@Composable
fun LastNameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false
) {
    BaseTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        placeholder = {
            Text(text = stringResource(Res.string.last_name_placeholder))
        },
        label = {
            TextFieldLabel(
                text = stringResource(Res.string.last_name_label),
                isError = isError
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = null
            )
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next
        ),
        isError = isError,
        supportingText = if (!isError) null else {
            {
                Text(text = stringResource(Res.string.last_name_error))
            }
        },
    )
}