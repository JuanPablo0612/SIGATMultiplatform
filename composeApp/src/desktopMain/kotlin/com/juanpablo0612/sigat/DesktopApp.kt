package com.juanpablo0612.sigat

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import com.juanpablo0612.sigat.ui.auth.DesktopAuthScreen
import com.juanpablo0612.sigat.ui.theme.SIGATTheme
import io.github.vinceglb.filekit.coil.addPlatformFileSupport
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun DesktopApp() {
    SIGATTheme {
        setSingletonImageLoaderFactory { context ->
            ImageLoader.Builder(context)
                .components {
                    addPlatformFileSupport()
                }
                .build()
        }

        Surface {
            DesktopAuthScreen()
        }
    }
}