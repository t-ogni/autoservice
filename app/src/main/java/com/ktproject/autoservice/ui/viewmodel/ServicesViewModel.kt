package com.ktproject.autoservice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.model.Service
import com.ktproject.autoservice.data.model.User
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
                _services.value = serviceRepository.getAllServices()
            }
        }
    }

    fun loadServiceById(serviceId: String) {
        viewModelScope.launch {
            _serviceState.value = UIState.Loading
            try {
                val service = serviceRepository.getServiceById(serviceId)
                if (service != null) {
                    _serviceState.value = UIState.Success(service)
                } else {
                    _serviceState.value = UIState.Error("Сервис не найден")
                }
            } catch (e: Exception) {
                _serviceState.value = UIState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }
}
