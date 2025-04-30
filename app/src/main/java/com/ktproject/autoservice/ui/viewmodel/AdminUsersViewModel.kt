package com.ktproject.autoservice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.model.User
import com.ktproject.autoservice.data.repository.UserRepository
import com.ktproject.autoservice.ui.components.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminUsersViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _usersState = MutableStateFlow<UIState<List<User>>>(UIState.Loading)
    val usersState: StateFlow<UIState<List<User>>> = _usersState

    fun loadAllUsers() {
        viewModelScope.launch {
            _usersState.value = UIState.Loading
            try {
                val users = userRepository.getAllUsers()
                _usersState.value = UIState.Success(users)
            } catch (e: Exception) {
                _usersState.value = UIState.Error(e.message ?: "Ошибка загрузки пользователей")
            }
        }
    }
}
