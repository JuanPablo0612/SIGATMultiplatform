package com.juanpablo0612.sigat

import com.juanpablo0612.sigat.data.actions.ActionsRepository
import com.juanpablo0612.sigat.data.actions.remote.ActionsRemoteDataSource
import com.juanpablo0612.sigat.data.auth.AuthRepository
import com.juanpablo0612.sigat.data.auth.remote.AuthRemoteDataSource
import com.juanpablo0612.sigat.data.obligations.ObligationsRepository
import com.juanpablo0612.sigat.data.obligations.remote.ObligationsRemoteDataSource
import com.juanpablo0612.sigat.data.roles.RolesRepository
import com.juanpablo0612.sigat.data.roles.remote.RolesRemoteDataSource
import com.juanpablo0612.sigat.data.users.UsersRepository
import com.juanpablo0612.sigat.data.users.remote.UsersRemoteDataSource
import com.juanpablo0612.sigat.ui.actions.add_action.AddActionViewModel
import com.juanpablo0612.sigat.ui.admin.manage_roles.ManageRolesViewModel
import com.juanpablo0612.sigat.ui.auth.login.LoginViewModel
import com.juanpablo0612.sigat.ui.auth.register.RegisterViewModel
import com.juanpablo0612.sigat.ui.home.HomeViewModel
import com.juanpablo0612.sigat.ui.navigation.AppNavigationViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.storage.storage
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::AddActionViewModel)
    viewModelOf(::AppNavigationViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::ManageRolesViewModel)
}

val dataModule = module {
    single { Firebase.auth }
    single { Firebase.storage }
    single { Firebase.firestore }
    singleOf(::AuthRemoteDataSource)
    singleOf(::AuthRepository)
    singleOf(::ActionsRemoteDataSource)
    singleOf(::ActionsRepository)
    singleOf(::RolesRemoteDataSource)
    singleOf(::RolesRepository)
    singleOf(::UsersRemoteDataSource)
    singleOf(::UsersRepository)
    singleOf(::ObligationsRemoteDataSource)
    singleOf(::ObligationsRepository)
}

fun initKoin(koinAppDeclaration: KoinAppDeclaration? = null) {
    startKoin {
        koinAppDeclaration?.invoke(this)
        modules(appModule, dataModule)
    }
}
