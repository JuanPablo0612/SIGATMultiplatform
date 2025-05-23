package com.juanpablo0612.sigat.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.juanpablo0612.sigat.ui.auth.login.DesktopLoginContent
import com.juanpablo0612.sigat.ui.auth.lregister.DesktopRegisterContent

@Composable
fun DesktopAuthScreen() {
    val gradient = if (isSystemInDarkTheme()) Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.background,
        )
    ) else Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.background
        )
    )

    Row(
        modifier = Modifier.fillMaxSize().background(gradient),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DesktopLoginContent(
            modifier = Modifier.weight(1f).padding(32.dp)
        )

        DesktopRegisterContent(
            modifier = Modifier.weight(1f).padding(32.dp)
        )
    }
}