package com.juanpablo0612.sigat.data.platform

import dev.gitlive.firebase.storage.Data

internal expect object StoragePlatform {
    fun dataOf(bytes: ByteArray): Data
}