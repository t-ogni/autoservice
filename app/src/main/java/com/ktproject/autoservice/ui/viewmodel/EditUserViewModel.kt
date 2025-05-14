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
    
    class EditUserViewModel(
        private val userRepository: UserRepository
    ) : ViewModel() {
    
        private val _userState = MutableStateFlow<UIState<User?>>(UIState.Loading)
        val userState: StateFlow<UIState<User?>> = _userState
    
        fun loadUser(userId: String) {
            viewModelScope.launch {
                _userState.value = UIState.Loading
                when (val result = userRepository.getUserById(userId)) {
                    is RepositoryResult.Success -> _userState.value = UIState.Success(result.data)
                    is RepositoryResult.Error -> _userState.value = UIState.Error(result.message)
                    is RepositoryResult.NetworkError -> _userState.value = UIState.Error(result.message)
                }
            }
        }
    
        fun updateUser(userId: String, name: String?, email: String?, role: String?) {
            viewModelScope.launch {
                when (val result = userRepository.updateUser(userId, name, email, role)) {
                    is RepositoryResult.Success -> loadUser(userId)
                    is RepositoryResult.Error -> _userState.value = UIState.Error(result.message)
                    is RepositoryResult.NetworkError -> _userState.value = UIState.Error(result.message)
                }
            }
        }
    
        fun deleteUser(userId: String, onDeleted: () -> Unit) {
            viewModelScope.launch {
                when (val result = userRepository.deleteUser(userId)) {
                    is RepositoryResult.Success -> onDeleted()
                    is RepositoryResult.Error -> _userState.value = UIState.Error(result.message)
                    is RepositoryResult.NetworkError -> _userState.value = UIState.Error(result.message)
                }
            }
        }
    }
