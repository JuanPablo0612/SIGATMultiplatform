package com.juanpablo0612.sigat.ui.training_programs.list

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.training_programs_title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingProgramListTopBar() {
    TopAppBar(
        title = {
            Text(text = stringResource(Res.string.training_programs_title))
        }
    )
}
