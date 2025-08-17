package com.juanpablo0612.sigat.ui.camera_launcher

import io.github.vinceglb.filekit.PlatformFile

expect val isCameraLauncherAvailable: Boolean

expect suspend fun launchCamera(): PlatformFile?