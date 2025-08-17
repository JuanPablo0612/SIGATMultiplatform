package com.juanpablo0612.sigat.ui.admin.manage_roles

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import sigat.composeapp.generated.resources.Res
import sigat.composeapp.generated.resources.manage_roles_title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageRolesTopBar() {
    TopAppBar(
        title = {
            Text(text = stringResource(Res.string.manage_roles_title))
        }
    )
}