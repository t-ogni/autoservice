    package com.ktproject.autoservice.ui.viewmodel

    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.ktproject.autoservice.data.model.User
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
                try {
                    val user = userRepository.getUserById(userId)
                    _userState.value = UIState.Success(user)
                } catch (e: Exception) {
                    _userState.value = UIState.Error(e.message ?: "Ошибка загрузки пользователя")
                }
            }
        }

        fun updateUser(userId: String, name: String?, email: String?, role: String?) {
            viewModelScope.launch {
                try {
                    userRepository.updateUser(userId, name, email, role)
                    loadUser(userId)
                } catch (e: Exception) {
                    _userState.value = UIState.Error(e.message ?: "Ошибка при обновлении")
                }
            }
        }


        fun deleteUser(userId: String, onDeleted: () -> Unit) {
            viewModelScope.launch {
                try {
                    userRepository.deleteUser(userId)
                    onDeleted()
                } catch (e: Exception) {
                    _userState.value = UIState.Error(e.message ?: "Ошибка при удалении")
                }
            }
        }
    }
