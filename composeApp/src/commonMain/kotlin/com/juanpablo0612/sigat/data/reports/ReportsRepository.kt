package com.juanpablo0612.sigat.data.reports

import com.juanpablo0612.sigat.data.reports.local.ReportsLocalDataSource
import com.juanpablo0612.sigat.domain.model.Action
import com.juanpablo0612.sigat.domain.model.User
import io.github.vinceglb.filekit.PlatformFile

class ReportsRepository(private val localDataSource: ReportsLocalDataSource) {
    suspend fun generateReport(
        template: PlatformFile,
        output: PlatformFile,
        user: User,
        actions: List<Action>
    ) {
        localDataSource.generateReport(
            template,
            output,
            user.toModel(),
            actions.map { it.toModel() })
    }
}