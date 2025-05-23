package com.juanpablo0612.sigat.data.actions.remote

import com.juanpablo0612.sigat.data.actions.model.ActionModel
import com.juanpablo0612.sigat.data.exceptions.handleExceptions
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.storage.File
import dev.gitlive.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ActionsRemoteDataSource(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    private val actionsCollection = firestore.collection("actions")

    @OptIn(ExperimentalUuidApi::class)
    suspend fun addAction(actionModel: ActionModel, images: List<File>) {
        handleExceptions {
            val actionId = actionsCollection.document.id

            val imageUrls = images.map { image ->
                val imageName = "${Uuid.random()}.png"
                uploadImage(
                    actionModel.dni,
                    actionId,
                    imageName,
                    image
                )
            }

            val newActionModel = actionModel.copy(
                id = actionId,
                images = imageUrls
            )

            actionsCollection.document(actionId).set(newActionModel)
        }
    }

    private suspend fun uploadImage(
        dni: String,
        actionId: String,
        imageName: String,
        file: File
    ): String {
        val storageRef = storage.reference
        val imageRef = storageRef.child("actions/$actionId/$imageName")
        imageRef.putFile(file)

        val downloadUrl = imageRef.getDownloadUrl()
        return downloadUrl
    }

    fun getActions(dni: String): Flow<List<ActionModel>> {
        return actionsCollection.where {
            "dni" equalTo dni
        }.snapshots.map { it.documents.map { document -> document.data(ActionModel.serializer()) } }
    }
}