package com.ktproject.autoservice.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.model.User
import com.ktproject.autoservice.data.repository.UserRepository
import com.ktproject.autoservice.ui.components.UIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
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
            try {
                _userState.value = UIState.Loading
                val user = userRepository.getCurrentUserId()
                if (user != null) {
                    _userState.value = UIState.Success(user)
                } else {
                    _userState.value = UIState.Error("User not found")
                }
            } catch (e: Exception) {
                _userState.value = UIState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                _logoutState.value = UIState.Loading
                userRepository.logout()
                _logoutState.value = UIState.Success(Unit)
            } catch (e: Exception) {
                _logoutState.value = UIState.Error(e.message ?: "Logout failed")
            }
        }
    }
}
