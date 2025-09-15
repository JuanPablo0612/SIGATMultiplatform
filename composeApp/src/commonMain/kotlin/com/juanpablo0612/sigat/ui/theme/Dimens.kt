package com.juanpablo0612.sigat.ui.theme

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Legacy dimensional constants.  New code should prefer [LocalSpacing]
 * which provides values that react to the current [WindowSizeClass].
 */
object Dimens {
    val PaddingExtraSmall = 4.dp
    val PaddingSmall = 8.dp
    val PaddingMedium = 16.dp
}

data class Spacing(
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }

@Composable
fun ProvideSpacing(windowSize: WindowSizeClass, content: @Composable () -> Unit) {
    val spacing = remember(windowSize) {
        when (windowSize.widthSizeClass) {
            WindowWidthSizeClass.Compact -> Spacing()
            WindowWidthSizeClass.Medium -> Spacing(
                extraSmall = 6.dp,
                small = 12.dp,
                medium = 24.dp,
            )
            WindowWidthSizeClass.Expanded -> Spacing(
                extraSmall = 8.dp,
                small = 16.dp,
                medium = 32.dp,
            )
            else -> Spacing()
        }
    }
    CompositionLocalProvider(LocalSpacing provides spacing, content = content)
}


