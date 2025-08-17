package com.juanpablo0612.sigat.ui.camera_launcher

import io.github.vinceglb.filekit.PlatformFile

actual val isCameraLauncherAvailable: Boolean = false

actual suspend fun launchCamera(): PlatformFile? = null