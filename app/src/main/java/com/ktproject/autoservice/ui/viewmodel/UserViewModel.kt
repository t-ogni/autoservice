package com.ktproject.autoservice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.model.User
import com.ktproject.autoservice.data.repository.RepositoryResult
import com.ktproject.autoservice.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _selectedUser = MutableStateFlow<User?>(null)
    val selectedUser: StateFlow<User?> = _selectedUser

    fun loadAllUsers() {
        viewModelScope.launch {
            when (val result = userRepository.getAllUsers()) {
                is RepositoryResult.Success -> _users.value = result.data
                is RepositoryResult.Error -> {} // Можно добавить обработку ошибки
                is RepositoryResult.NetworkError -> {} // Можно добавить обработку ошибки сети
            }
        }
    }

    fun loadUserById(userId: String) {
        viewModelScope.launch {
            when (val result = userRepository.getUserById(userId)) {
                is RepositoryResult.Success -> _selectedUser.value = result.data
                is RepositoryResult.Error -> {} // Можно добавить обработку ошибки
                is RepositoryResult.NetworkError -> {} // Можно добавить обработку ошибки сети
            }
        }
    }

    fun updateUser(userId: String, name: String?, email: String?) {
        viewModelScope.launch {
            when (val result = userRepository.updateUser(userId, name, email, null)) {
                is RepositoryResult.Success -> {
                    loadAllUsers() // перезагружаем список
                    loadUserById(userId) // перезагружаем пользователя
                }
                is RepositoryResult.Error -> {} // Можно добавить обработку ошибки
                is RepositoryResult.NetworkError -> {} // Можно добавить обработку ошибки сети
            }
        }
    }

    fun deleteUser(userId: String, onDeleted: () -> Unit) {
        viewModelScope.launch {
            when (val result = userRepository.deleteUser(userId)) {
                is RepositoryResult.Success -> {
                    loadAllUsers()
                    onDeleted()
                }
                is RepositoryResult.Error -> {} // Можно добавить обработку ошибки
                is RepositoryResult.NetworkError -> {} // Можно добавить обработку ошибки сети
            }
        }
    }
}
