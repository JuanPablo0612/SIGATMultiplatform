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
    object Home: Screen()

    @Serializable
    object AddAction: Screen()

    @Serializable
    object GenerateReport: Screen()

    @Serializable
    object AddTrainingProgram: Screen()

    @Serializable
    data class TrainingProgramDetail(val programId: String): Screen()
}