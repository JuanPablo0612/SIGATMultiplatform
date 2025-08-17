package com.juanpablo0612.sigat

import com.juanpablo0612.sigat.data.actions.ActionsRepository
import com.juanpablo0612.sigat.data.actions.remote.ActionsRemoteDataSource
import com.juanpablo0612.sigat.data.actions.remote.ActionsRemoteDataSourceImpl
import com.juanpablo0612.sigat.data.auth.AuthRepository
import com.juanpablo0612.sigat.data.auth.remote.AuthRemoteDataSource
import com.juanpablo0612.sigat.data.auth.remote.BaseAuthRemoteDataSource
import com.juanpablo0612.sigat.data.obligations.ObligationsRepository
import com.juanpablo0612.sigat.data.obligations.remote.ObligationsRemoteDataSource
import com.juanpablo0612.sigat.data.reports.ReportsRepository
import com.juanpablo0612.sigat.data.reports.local.ReportsLocalDataSource
import com.juanpablo0612.sigat.data.reports.local.ReportsLocalDataSourceImpl
import com.juanpablo0612.sigat.data.roles.RolesRepository
import com.juanpablo0612.sigat.data.roles.remote.RolesRemoteDataSource
import com.juanpablo0612.sigat.data.training_programs.TrainingProgramsRepository
import com.juanpablo0612.sigat.data.training_programs.remote.TrainingProgramsRemoteDataSource
import com.juanpablo0612.sigat.data.users.UsersRepository
import com.juanpablo0612.sigat.data.users.remote.UsersRemoteDataSource
import com.juanpablo0612.sigat.domain.usecase.auth.ValidateEmailUseCase
import com.juanpablo0612.sigat.domain.usecase.auth.ValidatePasswordUseCase
import com.juanpablo0612.sigat.domain.usecase.auth.ValidateFirstNameUseCase
import com.juanpablo0612.sigat.domain.usecase.auth.ValidateLastNameUseCase
import com.juanpablo0612.sigat.domain.usecase.auth.ValidateIdNumberUseCase
import com.juanpablo0612.sigat.domain.usecase.auth.ValidateIdIssuingLocationUseCase
import com.juanpablo0612.sigat.domain.usecase.auth.ValidateConfirmPasswordUseCase
import com.juanpablo0612.sigat.state_holders.UserStateHolder
import com.juanpablo0612.sigat.state_holders.UserStateHolderImpl
import com.juanpablo0612.sigat.ui.actions.action_list.ActionListViewModel
import com.juanpablo0612.sigat.ui.actions.add_action.AddActionViewModel
import com.juanpablo0612.sigat.ui.admin.manage_roles.ManageRolesViewModel
import com.juanpablo0612.sigat.ui.training_programs.add.AddTrainingProgramViewModel
import com.juanpablo0612.sigat.ui.training_programs.detail.TrainingProgramDetailViewModel
import com.juanpablo0612.sigat.ui.training_programs.list.TrainingProgramListViewModel
import com.juanpablo0612.sigat.ui.auth.login.LoginViewModel
import com.juanpablo0612.sigat.ui.auth.register.RegisterViewModel
import com.juanpablo0612.sigat.ui.home.HomeViewModel
import com.juanpablo0612.sigat.ui.navigation.AppNavigationViewModel
import com.juanpablo0612.sigat.ui.reports.generate_report.GenerateReportViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.storage.storage
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

expect val platformModule: Module

val stateHoldersModule = module {
    singleOf<UserStateHolder>(::UserStateHolderImpl)
}

val viewModelModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::AddActionViewModel)
    viewModelOf(::AppNavigationViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::ManageRolesViewModel)
    viewModelOf(::GenerateReportViewModel)
    viewModelOf(::ActionListViewModel)
    viewModelOf(::TrainingProgramListViewModel)
    viewModelOf(::AddTrainingProgramViewModel)
    viewModelOf(::TrainingProgramDetailViewModel)
}

val domainModule = module {
    singleOf(::ValidateEmailUseCase)
    singleOf(::ValidatePasswordUseCase)
    singleOf(::ValidateFirstNameUseCase)
    singleOf(::ValidateLastNameUseCase)
    singleOf(::ValidateIdNumberUseCase)
    singleOf(::ValidateIdIssuingLocationUseCase)
    singleOf(::ValidateConfirmPasswordUseCase)
}

val dataModule = module {
    single { Firebase.auth }
    single { Firebase.firestore }
    single { Firebase.storage }
    single<AuthRemoteDataSource> { BaseAuthRemoteDataSource(get()) }
    single<ActionsRemoteDataSource> { ActionsRemoteDataSourceImpl(get(), get()) }
    singleOf(::AuthRepository)
    singleOf(::ActionsRepository)
    singleOf(::RolesRemoteDataSource)
    singleOf(::RolesRepository)
    singleOf(::UsersRemoteDataSource)
    singleOf(::UsersRepository)
    singleOf(::ObligationsRemoteDataSource)
    singleOf(::ObligationsRepository)
    single<ReportsLocalDataSource> { ReportsLocalDataSourceImpl() }
    singleOf(::ReportsRepository)
    singleOf(::TrainingProgramsRemoteDataSource)
    singleOf(::TrainingProgramsRepository)
}

fun initKoin(koinAppDeclaration: KoinAppDeclaration? = null) {
    startKoin {
        koinAppDeclaration?.invoke(this)
        modules(platformModule, stateHoldersModule, viewModelModule, domainModule, dataModule)
    }
}
