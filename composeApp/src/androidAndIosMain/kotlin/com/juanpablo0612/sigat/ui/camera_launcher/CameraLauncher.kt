package com.juanpablo0612.sigat.ui.camera_launcher

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.openCameraPicker

actual val isCameraLauncherAvailable: Boolean = true

actual suspend fun launchCamera() = FileKit.openCameraPicker()
