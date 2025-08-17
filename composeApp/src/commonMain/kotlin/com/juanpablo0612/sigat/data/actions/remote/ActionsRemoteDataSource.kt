package com.juanpablo0612.sigat.data.actions.remote

import com.juanpablo0612.sigat.data.actions.model.ActionModel
import com.juanpablo0612.sigat.data.exceptions.handleExceptions
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.storage.Data
import dev.gitlive.firebase.storage.FirebaseStorage
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.ImageFormat
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface ActionsRemoteDataSource {
    suspend fun addAction(actionModel: ActionModel, images: List<PlatformFile>)
    suspend fun uploadImage(actionId: String, imageName: String, imageByteArray: ByteArray): String
    fun getActions(creatorUid: String): Flow<List<ActionModel>>
}

class ActionsRemoteDataSourceImpl(
    private val storage: FirebaseStorage,
    firestore: FirebaseFirestore,
) : ActionsRemoteDataSource {
    private val actionsCollection = firestore.collection("actions")

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun addAction(actionModel: ActionModel, images: List<PlatformFile>) {
        handleExceptions {
            val actionId = actionsCollection.document.id

            val imageUrls = images.map { image ->
                val imageName = "${Uuid.random()}.jpg"
                uploadImage(
                    actionId,
                    imageName,
                    image.readBytes()
                )
            }

            val newActionModel = actionModel.copy(
                id = actionId,
                images = imageUrls
            )

            actionsCollection.document(actionId).set(newActionModel)
        }
    }

    override suspend fun uploadImage(
        actionId: String,
        imageName: String,
        imageByteArray: ByteArray
    ): String {
        val compressedBytes = compressImage(imageByteArray)
        val imageRef = storage.reference.child("actions/$actionId/$imageName")
        imageRef.putData(Data(compressedBytes))
        return imageRef.getDownloadUrl()
    }

    override fun getActions(creatorUid: String): Flow<List<ActionModel>> {
        return actionsCollection.where {
            "creatorUid" equalTo creatorUid
        }.snapshots.map { it.documents.map { document -> document.data(ActionModel.serializer()) } }
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