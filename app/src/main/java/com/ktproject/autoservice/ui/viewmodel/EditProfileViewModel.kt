package com.ktproject.autoservice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.model.User
import com.ktproject.autoservice.data.repository.UserRepository
import com.ktproject.autoservice.ui.components.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userState = MutableStateFlow<UIState<User>>(UIState.Loading)
    val userState: StateFlow<UIState<User>> = _userState

    init {
        loadUser()
    }

    private fun loadUser() {
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

    fun updateProfile(name: String, email: String) {
        viewModelScope.launch {
            try {
                val user = (userState.value as? UIState.Success)?.data
                user?.let {
                    userRepository.updateUser(it.id, name, email)
                    loadUser() // Перезагрузить обновленные данные
                }
            } catch (e: Exception) {
                _userState.value = UIState.Error(e.message ?: "Update failed")
            }
        }
    }
}
