package com.juanpablo0612.sigat.data.reports.local

import com.juanpablo0612.sigat.data.actions.model.ActionModel
import com.juanpablo0612.sigat.data.users.model.UserModel
import io.github.vinceglb.filekit.PlatformFile

interface ReportsLocalDataSource {
    suspend fun generateReport(
        template: PlatformFile,
        output: PlatformFile,
        user: UserModel,
        actions: List<ActionModel>
    )
}

expect class ReportsLocalDataSourceImpl() : ReportsLocalDataSource {
    override suspend fun generateReport(
        template: PlatformFile,
        output: PlatformFile,
        user: UserModel,
        actions: List<ActionModel>
    )
}