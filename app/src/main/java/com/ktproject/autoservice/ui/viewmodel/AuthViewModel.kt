package com.ktproject.autoservice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.local.TokenDataStore
import com.ktproject.autoservice.data.model.LoginRequest
import com.ktproject.autoservice.data.repository.ResultState
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
            when (val result = userRepository.login(email, password)) {
                is ResultState.Success -> {
                    _authUiState.value = AuthUiState.Success
                    _navigationEvent.emit(Unit)
                }
                is ResultState.Error -> {
                    _authUiState.value = AuthUiState.Error(result.message)
                }
                ResultState.Loading -> {} // обычно не приходит сюда
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _authUiState.value = AuthUiState.Loading
            when (val result = userRepository.register(name, email, password)) {
                is ResultState.Success -> {
                    _authUiState.value = AuthUiState.Success
                    _navigationEvent.emit(Unit)
                }
                is ResultState.Error -> {
                    _authUiState.value = AuthUiState.Error(result.message)
                }
                ResultState.Loading -> {} // обычно не приходит сюда
            }
        }
    }

}
