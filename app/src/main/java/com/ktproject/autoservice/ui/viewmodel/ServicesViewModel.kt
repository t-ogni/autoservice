package com.ktproject.autoservice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.model.Service
import com.ktproject.autoservice.data.model.User
import com.ktproject.autoservice.data.repository.RepositoryResult
import com.ktproject.autoservice.data.repository.RequestRepository
import com.ktproject.autoservice.data.repository.ServiceRepository
import com.ktproject.autoservice.data.repository.fake.FakeServiceRepository
import com.ktproject.autoservice.ui.components.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ServicesViewModel (
    private val serviceRepository: ServiceRepository
) : ViewModel() {
    private val _services = MutableStateFlow<List<Service>>(emptyList())
    val services: StateFlow<List<Service>> = _services

    private val _selectedService = MutableStateFlow<Service?>(null)
    val selectedService: StateFlow<Service?> = _selectedService

    private val _serviceState = MutableStateFlow<UIState<Service>>(UIState.Loading)
    val serviceState: StateFlow<UIState<Service>> = _serviceState.asStateFlow()

    init {
        loadServices()
    }

    fun loadServices() {
        viewModelScope.launch {
            if (_services.value.isEmpty()) {
                when (val result = serviceRepository.getAllServices()) {
                    is RepositoryResult.Success -> _services.value = result.data
                    is RepositoryResult.Error -> {} // Можно добавить обработку ошибки
                    is RepositoryResult.NetworkError -> {} // Можно добавить обработку ошибки сети
                }
            }
        }
    }

    fun loadServiceById(serviceId: String) {
        viewModelScope.launch {
            _serviceState.value = UIState.Loading
            when (val result = serviceRepository.getServiceById(serviceId)) {
                is RepositoryResult.Success -> _serviceState.value = UIState.Success(result.data)
                is RepositoryResult.Error -> _serviceState.value = UIState.Error(result.message)
                is RepositoryResult.NetworkError -> _serviceState.value = UIState.Error(result.message)
            }
        }
    }
}
