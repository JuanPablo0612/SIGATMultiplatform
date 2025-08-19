package com.juanpablo0612.sigat

import android.app.Application
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.compose.resources.stringResource
import sigat.composeapp.generated.resources.Res
import com.google.firebase.Firebase
import com.google.firebase.FirebaseOptions
import com.google.firebase.FirebasePlatform
import com.google.firebase.initialize
import io.github.vinceglb.filekit.FileKit

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun main() {
    initializeFirebase()
    initKoin()
    application {
        FileKit.init(appId = "com.juanpablo0612.sigat")

        Window(
            onCloseRequest = ::exitApplication,
            title = stringResource(Res.string.app_name),
        ) {
            App(windowSize = calculateWindowSizeClass())
        }
    }
}

fun initializeFirebase() {
    FirebasePlatform.initializeFirebasePlatform(object : FirebasePlatform() {
        val storage = mutableMapOf<String, String>()
        override fun store(key: String, value: String) = storage.set(key, value)
        override fun retrieve(key: String) = storage[key]
        override fun clear(key: String) {
            storage.remove(key)
        }

        override fun log(msg: String) = println(msg)
    })

    val options = FirebaseOptions.Builder()
        .setProjectId("sigat-1bf29")
        .setApplicationId("1:336608379836:android:295673f35d816adbce21b9")
        .setApiKey("AIzaSyDs0WYjwp9LK6FJB-F331W5Z_YRnp4ZntU")
        .build()

    val app = Firebase.initialize(Application(), options)
}