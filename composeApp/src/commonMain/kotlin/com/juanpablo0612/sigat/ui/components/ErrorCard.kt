package com.juanpablo0612.sigat.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.juanpablo0612.sigat.ui.theme.LocalSpacing

@Composable
fun ErrorCard(
    message: String,
    modifier: Modifier = Modifier,
    title: String? = null
) {
    val spacing = LocalSpacing.current
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    ) {
        Column(modifier = Modifier.padding(spacing.medium)) {
            title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}