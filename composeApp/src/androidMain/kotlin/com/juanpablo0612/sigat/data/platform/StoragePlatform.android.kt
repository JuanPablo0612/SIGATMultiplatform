package com.juanpablo0612.sigat.data.platform

import dev.gitlive.firebase.storage.Data

internal actual object StoragePlatform {
    actual fun dataOf(bytes: ByteArray): Data {
        return Data(bytes)
    }
}