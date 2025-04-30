package com.ktproject.autoservice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.local.TokenDataStore
import com.ktproject.autoservice.data.model.LoginRequest
import com.ktproject.autoservice.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    object Success : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _authUiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val authUiState: StateFlow<AuthUiState> = _authUiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<Unit>()
    val navigationEvent: SharedFlow<Unit> = _navigationEvent.asSharedFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authUiState.value = AuthUiState.Loading
            try {
                val response = userRepository.login(email, password)
                if (response == true) {
                    _authUiState.value = AuthUiState.Success
                    _navigationEvent.emit(Unit)
                } else {
                    _authUiState.value = AuthUiState.Error("Вход не выполнен")
                }
            } catch (e: Exception) {
                _authUiState.value = AuthUiState.Error("Ошибка входа: ${e.message}")

            }
        }
    }
    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _authUiState.value = AuthUiState.Loading
            try {
                val success = userRepository.register(name, email, password)
                if (success) {
                    _authUiState.value = AuthUiState.Success
                    _navigationEvent.emit(Unit)
                } else {
                    _authUiState.value = AuthUiState.Error("Email уже используется")
                }
            } catch (e: Exception) {
                _authUiState.value = AuthUiState.Error("Ошибка регистрации: ${e.message}")
            }
        }
    }

}
