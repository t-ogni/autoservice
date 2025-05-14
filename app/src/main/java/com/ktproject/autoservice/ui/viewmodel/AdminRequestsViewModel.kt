package com.ktproject.autoservice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.model.Request
import com.ktproject.autoservice.data.model.Service
import com.ktproject.autoservice.data.repository.RepositoryResult
import com.ktproject.autoservice.data.repository.RequestRepository
import com.ktproject.autoservice.data.repository.ServiceRepository
import com.ktproject.autoservice.ui.components.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminRequestsViewModel(
    private val requestRepository: RequestRepository,
    private val serviceRepository: ServiceRepository
) : ViewModel() {

    private val _requestsState = MutableStateFlow<UIState<List<Request>>>(UIState.Loading)
    val requestsState: StateFlow<UIState<List<Request>>> = _requestsState

    private val _selectedRequest = MutableStateFlow<Request?>(null)
    val selectedRequest: StateFlow<Request?> = _selectedRequest

    private val _servicesMap = MutableStateFlow<Map<String, Service>>(emptyMap())
    val servicesMap: StateFlow<Map<String, Service>> = _servicesMap

    private val _updateOperationState = MutableStateFlow<UIState<Unit>?>(null)
    val updateOperationState: StateFlow<UIState<Unit>?> = _updateOperationState

    // Список возможных статусов для выбора
    val availableStatuses = listOf("wait", "active", "completed", "canceled")

    init {
        loadServices()
    }

    private fun loadServices() {
        viewModelScope.launch {
            when (val result = serviceRepository.getAllServices()) {
                is RepositoryResult.Success -> {
                    _servicesMap.value = result.data.associateBy { it.id }
                }
                else -> {} // Можно добавить обработку ошибок
            }
        }
    }

    fun getServiceNameById(serviceId: String): String {
        return _servicesMap.value[serviceId]?.title ?: "Неизвестная услуга (#$serviceId)"
    }

    fun loadAllRequests() {
        viewModelScope.launch {
            _requestsState.value = UIState.Loading
            when (val result = requestRepository.getAllRequests()) {
                is RepositoryResult.Success -> _requestsState.value = UIState.Success(result.data)
                is RepositoryResult.Error -> _requestsState.value = UIState.Error(result.message)
                is RepositoryResult.NetworkError -> _requestsState.value = UIState.Error(result.message)
            }
        }
    }

    fun loadRequestById(id: String) {
        viewModelScope.launch {
            val result = requestRepository.getAllRequests()
            when (result) {
                is RepositoryResult.Success -> _selectedRequest.value = result.data.find { it.id == id }
                is RepositoryResult.Error -> _selectedRequest.value = null
                is RepositoryResult.NetworkError -> _selectedRequest.value = null
            }
        }
    }

    fun updateRequestStatus(id: String, status: String, result: String?) {
        viewModelScope.launch {
            _updateOperationState.value = UIState.Loading
            val updateResult = requestRepository.updateRequestStatus(id, status, result)
            when (updateResult) {
                is RepositoryResult.Success -> {
                    loadRequestById(id) // Обновляем детали текущей заявки
                    _updateOperationState.value = UIState.Success(Unit)
                }
                is RepositoryResult.Error -> {
                    _updateOperationState.value = UIState.Error(updateResult.message)
                }
                is RepositoryResult.NetworkError -> {
                    _updateOperationState.value = UIState.Error(updateResult.message)
                }
            }
        }
    }

    fun resetUpdateState() {
        _updateOperationState.value = null
    }
}
