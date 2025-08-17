package com.juanpablo0612.sigat.state_holders

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.juanpablo0612.sigat.domain.model.User

data class UserState(
    val isLoggedIn: Boolean = false,
    val user: User? = null
)

interface UserStateHolder {
    val userState: UserState
    fun updateUser(user: User)
    fun clearUser()
}

class UserStateHolderImpl : UserStateHolder {
    override var userState by mutableStateOf(UserState())
        private set

    override fun updateUser(user: User) {
        userState = userState.copy(isLoggedIn = true, user = user)
    }

    override fun clearUser() {
        userState = UserState()
    }
}