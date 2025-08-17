package com.juanpablo0612.sigat.data.actions.remote

import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.storage.FirebaseStorage
import dev.gitlive.firebase.storage.android
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.ImageFormat
import io.github.vinceglb.filekit.compressImage
import kotlinx.coroutines.tasks.await

class AndroidActionsRemoteDataSource(
    private val storage: FirebaseStorage,
    firestore: FirebaseFirestore
) : BaseActionsRemoteDataSource(firestore) {
    override suspend fun uploadImage(
        actionId: String,
        imageName: String,
        imageByteArray: ByteArray
    ): String {
        val compressedBytes = compressImage(imageByteArray)
        val storageRef = storage.android.reference
        val imageRef = storageRef.child("actions/$actionId/$imageName")
        imageRef.putBytes(compressedBytes).await()

        val downloadUrl = imageRef.getDownloadUrl().await()
        return downloadUrl.toString()
    }

    private suspend fun compressImage(imageBytes: ByteArray): ByteArray {
        return FileKit.compressImage(
            bytes = imageBytes,
            quality = 80,
            maxWidth = 1024,
            maxHeight = 1024,
            imageFormat = ImageFormat.JPEG
        )
    }
}