package com.juanpablo0612.sigat.data.actions.remote

import dev.gitlive.firebase.firestore.FirebaseFirestore

class DesktopActionsRemoteDataSource(firestore: FirebaseFirestore) :
    BaseActionsRemoteDataSource(firestore) {
    override suspend fun uploadImage(
        actionId: String,
        imageName: String,
        imageByteArray: ByteArray
    ): String {
        TODO("Not yet implemented")
    }
}