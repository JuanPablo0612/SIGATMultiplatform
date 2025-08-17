package com.juanpablo0612.sigat

import com.juanpablo0612.sigat.data.actions.remote.ActionsRemoteDataSource
import com.juanpablo0612.sigat.data.actions.remote.DesktopActionsRemoteDataSource
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<ActionsRemoteDataSource> { DesktopActionsRemoteDataSource(get()) }
}