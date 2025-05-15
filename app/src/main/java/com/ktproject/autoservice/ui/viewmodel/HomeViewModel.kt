package com.ktproject.autoservice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.model.News
import com.ktproject.autoservice.data.model.Request
import com.ktproject.autoservice.data.model.Service
import com.ktproject.autoservice.data.repository.NewsRepository
import com.ktproject.autoservice.data.repository.RequestRepository
import com.ktproject.autoservice.data.repository.RepositoryResult
import com.ktproject.autoservice.data.repository.ServiceRepository
import com.ktproject.autoservice.data.repository.UserRepository
import com.ktproject.autoservice.ui.components.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class HomeUiState {
    object Idle : HomeUiState()
    object Loading : HomeUiState()
    object Success : HomeUiState()
}

data class HomeUiStateData(
    val homeUiState: HomeUiState = HomeUiState.Loading,
    val userRole: String = "",
    val myRequests: List<Request> = emptyList(),
    var newsList: List<News> = emptyList()
)
class HomeViewModel(
    private val userRepository: UserRepository,
    private val newsRepository: NewsRepository,
    private val requestRepository: RequestRepository,
    private val serviceRepository: ServiceRepository
) : ViewModel() {

    private val _requestsUiState = MutableStateFlow<UIState<List<Request>>>(UIState.Loading)
    val requestsUiState: StateFlow<UIState<List<Request>>> = _requestsUiState

    private val _newsUiState = MutableStateFlow<UIState<List<News>>>(UIState.Loading)
    val newsUiState: StateFlow<UIState<List<News>>> = _newsUiState

    private val _userRole = MutableStateFlow<UIState<String>>(UIState.Loading)
    val userRole: StateFlow<UIState<String>> = _userRole
    
    // Карта сервисов для быстрого доступа по ID
    private val _servicesMap = MutableStateFlow<Map<String, Service>>(emptyMap())

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            loadUserRole()
            loadRequests()
            loadNews()
            loadServices()
        }
    }
    
    private fun loadServices() {
        viewModelScope.launch {
            try {
                when (val result = serviceRepository.getAllServices()) {
                    is RepositoryResult.Success -> {
                        _servicesMap.value = result.data.associateBy { it.id }
                    }
                    is RepositoryResult.Error -> {
                        // Тихая обработка ошибки
                    }
                    is RepositoryResult.NetworkError -> {
                        // Тихая обработка ошибки
                    }
                }
            } catch (e: Exception) {
                // Игнорируем ошибки при загрузке сервисов
            }
        }
    }
    
    fun getServiceNameById(serviceId: String): String {
        return _servicesMap.value[serviceId]?.title ?: "Услуга #$serviceId"
    }

    private suspend fun loadUserRole() {
        _userRole.value = UIState.Loading
        try {
            val result = userRepository.getCurrentUser()
            when (result) {
                is RepositoryResult.Success -> { _userRole.value = UIState.Success(result.data.role) }
                is RepositoryResult.Error -> { _userRole.value = UIState.Error(result.message) }
                is RepositoryResult.NetworkError -> { _userRole.value = UIState.Error(result.message) }
            }

        } catch (e: Exception) {
            _userRole.value = UIState.Error("Ошибка загрузки пользователя: ${e.message}")
        }
    }

    fun loadRequests() {
        viewModelScope.launch {
            _requestsUiState.value = UIState.Loading
            try {
                when (val result = requestRepository.getMyRequests()) {
                    is RepositoryResult.Success -> { _requestsUiState.value = UIState.Success(result.data) }
                    is RepositoryResult.Error -> { _requestsUiState.value = UIState.Error(result.message) }
                    is RepositoryResult.NetworkError -> { _requestsUiState.value = UIState.Error(result.message) }
                }

            } catch (e: Exception) {
                _requestsUiState.value = UIState.Error("Ошибка загрузки заявок: ${e.message}")
            }
        }
    }

    fun loadNews() {
        viewModelScope.launch {
            _newsUiState.value = UIState.Loading
            when (val result = newsRepository.getNews()) {
                is RepositoryResult.Success -> {
                    _newsUiState.value = UIState.Success(result.data.sortedByDescending { it.date })
                }
                is RepositoryResult.Error -> {
                    _newsUiState.value = UIState.Error("Ошибка загрузки новостей: ${result.message}")
                }
                is RepositoryResult.NetworkError -> {}
            }
        }
    }
}
