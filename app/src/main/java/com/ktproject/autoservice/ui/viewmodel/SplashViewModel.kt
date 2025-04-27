package com.ktproject.autoservice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.local.TokenDataStore
import com.ktproject.autoservice.data.remote.ApiClient
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class SplashViewModel(
    private val tokenDataStore: TokenDataStore,
    private val apiClient: ApiClient
) : ViewModel() {

    sealed class SplashState {
        object Authenticated : SplashState()
        object Unauthenticated : SplashState()
    }

    private var _state: ((SplashState) -> Unit)? = null

    // Метод для наблюдения за состоянием
    fun observeState(listener: (SplashState) -> Unit) {
        _state = listener
    }

    // Проверка наличия токена
    fun checkAuth() {
        viewModelScope.launch {
            // Получаем токен из DataStore
            val token = tokenDataStore.token.firstOrNull()

            if (!token.isNullOrEmpty()) {
                // Если токен есть, обновляем его в ApiClient
                apiClient.updateToken(token)
                // Вызываем состояние "Authenticated"
                _state?.invoke(SplashState.Authenticated)
            } else {
                // Если токен отсутствует, вызываем состояние "Unauthenticated"
                _state?.invoke(SplashState.Unauthenticated)
            }
        }
    }
}
