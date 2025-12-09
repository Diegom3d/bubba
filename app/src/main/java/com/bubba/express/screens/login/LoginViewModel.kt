package com.bubba.express.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bubba.express.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged ->
                _state.value = state.value.copy(email = event.value)

            is LoginEvent.PasswordChanged ->
                _state.value = state.value.copy(password = event.value)

            LoginEvent.Submit -> login()
        }
    }

    private fun login() {
        val email = state.value.email
        val password = state.value.password

        if (email.isBlank() || password.isBlank()) {
            _state.value = state.value.copy(error = "Todos los campos son obligatorios")
            return
        }

        _state.value = state.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            val result = repo.login(email, password)

            result.fold(
                onSuccess = {
                    _state.value = state.value.copy(
                        isLoading = false,
                        isLoggedIn = true
                    )
                },
                onFailure = {
                    _state.value = state.value.copy(
                        isLoading = false,
                        error = "Credenciales incorrectas"
                    )
                }
            )
        }
    }
}
