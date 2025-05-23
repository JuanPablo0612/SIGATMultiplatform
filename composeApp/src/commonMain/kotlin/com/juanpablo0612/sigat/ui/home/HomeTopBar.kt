package com.juanpablo0612.sigat.ui.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(featureName: String) {
    TopAppBar(
        title = {
            Text(
                text = featureName,
                style = MaterialTheme.typography.titleLarge
            )
        }
    )
}