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
            _userState.value = UIState.Loading
            when (val result = userRepository.getCurrentUser()) {
                is RepositoryResult.Success -> _userState.value = UIState.Success(result.data)
                is RepositoryResult.Error -> _userState.value = UIState.Error(result.message)
                is RepositoryResult.NetworkError -> _userState.value = UIState.Error(result.message)
            }
        }
    }

    fun updateProfile(name: String, email: String) {
        viewModelScope.launch {
            val user = (userState.value as? UIState.Success)?.data
            user?.let {
                when (val result = userRepository.updateUser(it.id, name, email, user.role)) {
                    is RepositoryResult.Success -> loadUser() // Перезагрузить обновленные данные
                    is RepositoryResult.Error -> _userState.value = UIState.Error(result.message)
                    is RepositoryResult.NetworkError -> _userState.value = UIState.Error(result.message)
                }
            }
        }
    }
}
