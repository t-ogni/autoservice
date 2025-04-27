package com.ktproject.autoservice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.local.TokenDataStore
import com.ktproject.autoservice.data.model.LoginRequest
import com.ktproject.autoservice.data.remote.ApiClient
import com.ktproject.autoservice.data.repository.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val tokenDataStore: TokenDataStore,
    private val userRepository: UserRepository, // Заменили UserApi на UserRepository
    private val apiClient: ApiClient
) : ViewModel() {

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val response = userRepository.login(LoginRequest(email, password)) // Используем UserRepository
            tokenDataStore.saveToken(response.token)
            apiClient.updateToken(response.token)
        }
    }
}
