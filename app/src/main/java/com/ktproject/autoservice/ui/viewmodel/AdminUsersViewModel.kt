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

class AdminUsersViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _usersState = MutableStateFlow<UIState<List<User>>>(UIState.Loading)
    val usersState: StateFlow<UIState<List<User>>> = _usersState

    fun loadAllUsers() {
        viewModelScope.launch {
            _usersState.value = UIState.Loading
            when (val result = userRepository.getAllUsers()) {
                is RepositoryResult.Success -> _usersState.value = UIState.Success(result.data)
                is RepositoryResult.Error -> _usersState.value = UIState.Error(result.message)
                is RepositoryResult.NetworkError -> _usersState.value = UIState.Error(result.message)
            }
        }
    }
}
