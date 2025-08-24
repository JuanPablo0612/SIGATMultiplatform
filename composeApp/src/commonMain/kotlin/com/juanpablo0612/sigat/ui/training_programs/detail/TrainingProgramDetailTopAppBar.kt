package com.juanpablo0612.sigat.ui.training_programs.detail

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
import sigat.composeapp.generated.resources.content_description_navigate_back
import sigat.composeapp.generated.resources.training_program_detail_title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingProgramDetailTopAppBar(onNavigateBack: () -> Unit) {
    TopAppBar(
        title = { Text(stringResource(Res.string.training_program_detail_title)) },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(Res.string.content_description_navigate_back)
                )
            }
        }
    )

}