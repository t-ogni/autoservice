package com.ktproject.autoservice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.model.User
import com.ktproject.autoservice.data.repository.RepositoryResult
import com.ktproject.autoservice.data.repository.UserRepository
import com.ktproject.autoservice.ui.components.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userState = MutableStateFlow<UIState<User>>(UIState.Loading)
    val userState: StateFlow<UIState<User>> = _userState

    private val _logoutState = MutableStateFlow<UIState<Unit>>(UIState.Loading)
    val logoutState: StateFlow<UIState<Unit>> = _logoutState

    init {
        loadCurrentUser()
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            _userState.value = UIState.Loading
            when (val result = userRepository.getCurrentUser()) {
                is RepositoryResult.Success -> _userState.value = UIState.Success(result.data)
                is RepositoryResult.Error -> _userState.value = UIState.Error(result.message)
                is RepositoryResult.NetworkError -> _userState.value = UIState.Error(result.message)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _logoutState.value = UIState.Loading
            when (val result = userRepository.logout()) {
                is RepositoryResult.Success -> _logoutState.value = UIState.Success(Unit)
                is RepositoryResult.Error -> _logoutState.value = UIState.Error(result.message)
                is RepositoryResult.NetworkError -> _logoutState.value = UIState.Error(result.message)
            }
        }
    }
}
