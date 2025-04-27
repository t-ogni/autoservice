package com.ktproject.autoservice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.local.TokenDataStore
import com.ktproject.autoservice.data.model.LoginRequest
import com.ktproject.autoservice.data.remote.ApiClient
import com.ktproject.autoservice.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Success(val token: String) : UiState()
    data class Error(val message: String) : UiState()
}

class AuthViewModel(
    private val tokenDataStore: TokenDataStore,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<Unit>()
    val navigationEvent: SharedFlow<Unit> = _navigationEvent.asSharedFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = userRepository.login(LoginRequest(email, password))
                tokenDataStore.saveToken(response.token)
                _uiState.value = UiState.Success(response.token)
                _navigationEvent.emit(Unit)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Ошибка входа: ${e.message}")
            }
        }
    }
}
