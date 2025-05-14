package com.ktproject.autoservice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.local.TokenDataStore
import com.ktproject.autoservice.data.remote.ApiClient
import com.ktproject.autoservice.data.repository.RepositoryResult
import com.ktproject.autoservice.data.repository.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
class SplashViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    sealed class SplashState {
        object Authenticated : SplashState()
        object Unauthenticated : SplashState()
    }

    private var _state: ((SplashState) -> Unit)? = null

    fun observeState(listener: (SplashState) -> Unit) {
        _state = listener
    }

    fun checkAuth() {
        viewModelScope.launch {
            when (val result = userRepository.isAuthenticated()) {
                is RepositoryResult.Success -> {
                    if (result.data) {
                        _state?.invoke(SplashState.Authenticated)
                    } else {
                        _state?.invoke(SplashState.Unauthenticated)
                    }
                }
                is RepositoryResult.Error,
                is RepositoryResult.NetworkError -> {
                    _state?.invoke(SplashState.Unauthenticated)
                }
            }
        }
    }
}

