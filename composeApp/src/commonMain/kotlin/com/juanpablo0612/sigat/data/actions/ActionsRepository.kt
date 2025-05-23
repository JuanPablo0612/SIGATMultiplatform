package com.juanpablo0612.sigat.data.actions

import com.juanpablo0612.sigat.data.actions.remote.ActionsRemoteDataSource
import com.juanpablo0612.sigat.domain.model.Action
import dev.gitlive.firebase.storage.File
import kotlinx.coroutines.flow.map

class ActionsRepository(private val dataSource: ActionsRemoteDataSource) {
    suspend fun addAction(action: Action, images: List<File>) {
        val actionModel = action.toModel()
        dataSource.addAction(
            actionModel = actionModel,
            images = images
        )
    }

    fun getActions(dni: String) =
        dataSource.getActions(dni = dni).map { it.map { model -> model.toDomain() } }
}