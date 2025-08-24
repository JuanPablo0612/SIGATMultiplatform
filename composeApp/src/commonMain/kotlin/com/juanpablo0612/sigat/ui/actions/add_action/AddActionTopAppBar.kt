package com.juanpablo0612.sigat.ui.actions.add_action

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.button_add_action
import sigat.composeapp.generated.resources.content_description_navigate_back

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddActionTopAppBar(onBack: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(Res.string.button_add_action)) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(Res.string.content_description_navigate_back)
                )
            }
        }
    )
}