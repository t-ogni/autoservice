package com.ktproject.autoservice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.model.Service
import com.ktproject.autoservice.data.repository.RepositoryResult
import com.ktproject.autoservice.data.repository.ServiceRepository
import com.ktproject.autoservice.ui.components.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminServiceViewModel(
    private val repository: ServiceRepository
) : ViewModel() {

    private val _servicesState = MutableStateFlow<UIState<List<Service>>>(UIState.Loading)
    val servicesState: StateFlow<UIState<List<Service>>> = _servicesState

    private val _selectedService = MutableStateFlow<Service?>(null)
    val selectedService: StateFlow<Service?> = _selectedService

    fun loadAllServices() {
        viewModelScope.launch {
            _servicesState.value = UIState.Loading
            when (val result = repository.getAllServices()) {
                is RepositoryResult.Success -> _servicesState.value = UIState.Success(result.data)
                is RepositoryResult.Error -> _servicesState.value = UIState.Error(result.message)
                is RepositoryResult.NetworkError -> _servicesState.value = UIState.Error(result.message)
            }
        }
    }

    fun loadServiceById(id: String) {
        viewModelScope.launch {
            when (val result = repository.getServiceById(id)) {
                is RepositoryResult.Success -> _selectedService.value = result.data
                is RepositoryResult.Error -> {} // Обработка ошибки, если необходимо
                is RepositoryResult.NetworkError -> {} // Обработка ошибки сети, если необходимо
            }
        }
    }

    fun addService(name: String, description: String, price: String) {
        viewModelScope.launch {
            when (val result = repository.addService(name, description, price)) {
                is RepositoryResult.Success -> loadAllServices()
                is RepositoryResult.Error -> {} // Обработка ошибки, если необходимо
                is RepositoryResult.NetworkError -> {} // Обработка ошибки сети, если необходимо
            }
        }
    }

    fun deleteService(id: String) {
        viewModelScope.launch {
            when (val result = repository.deleteService(id)) {
                is RepositoryResult.Success -> loadAllServices()
                is RepositoryResult.Error -> {} // Обработка ошибки, если необходимо
                is RepositoryResult.NetworkError -> {} // Обработка ошибки сети, если необходимо
            }
        }
    }
}
