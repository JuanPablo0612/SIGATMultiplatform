package com.juanpablo0612.sigat.data.auth.remote

import dev.gitlive.firebase.auth.FirebaseAuth

interface AuthRemoteDataSource {
    suspend fun login(email: String, password: String): String
    suspend fun register(email: String, password: String): String
    suspend fun logout()
    fun isSignedIn(): Boolean
    fun getUid(): String
}

class BaseAuthRemoteDataSource(private val auth: FirebaseAuth) :
    AuthRemoteDataSource {
    override suspend fun login(email: String, password: String): String {
        return auth.signInWithEmailAndPassword(email, password).user!!.uid
    }

    override suspend fun register(email: String, password: String): String {
        return auth.createUserWithEmailAndPassword(email, password).user!!.uid
    }

    override suspend fun logout() {
        auth.signOut()
    }

    override fun isSignedIn(): Boolean {
        return auth.currentUser != null
    }

    override fun getUid(): String {
        return auth.currentUser!!.uid
    }
}