package com.bubba.express.screens.login

sealed class LoginEvent {
    data class EmailChanged(val value: String) : LoginEvent()
    data class PasswordChanged(val value: String) : LoginEvent()
    object Submit : LoginEvent()
}
