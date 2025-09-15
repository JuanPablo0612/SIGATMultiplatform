package com.juanpablo0612.sigat.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

/**
 * Returns a [Typography] instance that scales some text sizes based on the
 * provided [WindowSizeClass].  The default Material 3 typography is used for
 * compact screens while larger breakpoints slightly increase the base size.
 */
fun appTypography(windowSize: WindowSizeClass): Typography {
    return when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Medium -> Typography(
            bodyLarge = TextStyle(fontSize = 18.sp)
        )
        WindowWidthSizeClass.Expanded -> Typography(
            bodyLarge = TextStyle(fontSize = 20.sp)
        )
        else -> Typography()
    }
}

