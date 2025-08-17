package com.juanpablo0612.sigat.ui.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    object Login: Screen()

    @Serializable
    object Register: Screen()

    @Serializable
    object EmailVerification: Screen()

    @Serializable
    object EditProfile: Screen()

    @Serializable
    object AddAction: Screen()

    @Serializable
    object AddTrainingProgram: Screen()

    @Serializable
    data class TrainingProgramDetail(val programId: String): Screen()

    @Serializable
    object ManageRoles : Screen()

    @Serializable
    object TrainingPrograms : Screen()

    @Serializable
    object Actions : Screen()

    @Serializable
    object Reports : Screen()
}
