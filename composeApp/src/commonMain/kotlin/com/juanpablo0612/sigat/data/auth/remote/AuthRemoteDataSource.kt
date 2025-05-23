package com.juanpablo0612.sigat.data.auth.remote

import dev.gitlive.firebase.auth.FirebaseAuth

class AuthRemoteDataSource(private val auth: FirebaseAuth) {
    suspend fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
    }

    suspend fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
    }

    suspend fun logout() {
        auth.signOut()
    }

    fun isSignedIn(): Boolean {
        return auth.currentUser != null;
    }

    fun getUid(): String {
        return auth.currentUser!!.uid
    }
}