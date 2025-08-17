package com.juanpablo0612.sigat

import com.juanpablo0612.sigat.data.actions.remote.ActionsRemoteDataSource
import com.juanpablo0612.sigat.data.actions.remote.AndroidActionsRemoteDataSource
import org.koin.dsl.module

actual val platformModule = module {
    single<ActionsRemoteDataSource> { AndroidActionsRemoteDataSource(get(), get()) }
}