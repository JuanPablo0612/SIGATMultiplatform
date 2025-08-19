package com.juanpablo0612.sigat.data.reports.local

import com.juanpablo0612.sigat.data.actions.model.ActionModel
import com.juanpablo0612.sigat.data.users.model.UserModel
import com.juanpablo0612.sigat.data.contracts.model.ContractModel
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readBytes
import io.github.vinceglb.filekit.write

actual class ReportsLocalDataSourceImpl actual constructor() : ReportsLocalDataSource {
    actual override suspend fun generateReport(
        template: PlatformFile,
        output: PlatformFile,
        user: UserModel,
        contract: ContractModel,
        startTimestamp: Long,
        endTimestamp: Long,
        actions: List<ActionModel>
    ) {
        // Desktop implementation simply copies the template to the output file
        output.write(template.readBytes())
    }
}
