package com.juanpablo0612.sigat.data.sessions.model

import dev.gitlive.firebase.firestore.Timestamp

data class SessionModel(
    val id: String = "",
    val trainingProgramId: String = "",
    val startsAt: Timestamp = Timestamp.now(),
    val endsAt: Timestamp = Timestamp.now()
)