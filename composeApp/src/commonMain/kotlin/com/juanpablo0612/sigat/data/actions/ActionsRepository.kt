package com.juanpablo0612.sigat.data.actions

import com.juanpablo0612.sigat.data.actions.remote.ActionsRemoteDataSource
import com.juanpablo0612.sigat.domain.model.Action
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.map

class ActionsRepository(private val dataSource: ActionsRemoteDataSource) {
    suspend fun addAction(action: Action, images: List<PlatformFile>) {
        val actionModel = action.toModel()
        dataSource.addAction(
            actionModel = actionModel,
            images = images
        )
    }

    fun getActions(uid: String) =
        dataSource.getActions(creatorUid = uid).map { it.map { model -> model.toDomain() } }
}