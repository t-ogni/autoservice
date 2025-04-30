package com.ktproject.autoservice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.model.User
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
            _users.value = userRepository.getAllUsers()
        }
    }

    fun loadUserById(userId: String) {
        viewModelScope.launch {
            _selectedUser.value = userRepository.getUserById(userId)
        }
    }

    fun updateUser(userId: String, name: String?, email: String?) {
        viewModelScope.launch {
            userRepository.updateUser(userId, name, email)
            loadAllUsers() // перезагружаем список
            loadUserById(userId) // перезагружаем пользователя
        }
    }

    fun deleteUser(userId: String, onDeleted: () -> Unit) {
        viewModelScope.launch {
            userRepository.deleteUser(userId)
            loadAllUsers()
            onDeleted()
        }
    }
}
