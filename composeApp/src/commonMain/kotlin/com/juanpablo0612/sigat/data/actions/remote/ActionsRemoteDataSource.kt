package com.juanpablo0612.sigat.data.actions.remote

import com.juanpablo0612.sigat.data.actions.model.ActionModel
import com.juanpablo0612.sigat.data.exceptions.handleExceptions
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.storage.File
import dev.gitlive.firebase.storage.storage
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

abstract class BaseActionsRemoteDataSource(
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

    override fun getActions(creatorUid: String): Flow<List<ActionModel>> {
        return actionsCollection.where {
            "creatorUid" equalTo creatorUid
        }.snapshots.map { it.documents.map { document -> document.data(ActionModel.serializer()) } }
    }
}