package com.ktproject.autoservice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.model.Service
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
            try {
                _servicesState.value = UIState.Success(repository.getAllServices())
            } catch (e: Exception) {
                _servicesState.value = UIState.Error(e.message ?: "Ошибка при загрузке")
            }
        }
    }

    fun loadServiceById(id: String) {
        viewModelScope.launch {
            _selectedService.value = repository.getServiceById(id)
        }
    }

    fun addService(name: String, description: String, price: String) {
        viewModelScope.launch {
            try {
                repository.addService(name, description, price)
                loadAllServices()
            } catch (_: Exception) {}
        }
    }

    fun deleteService(id: String) {
        viewModelScope.launch {
            try {
                repository.deleteService(id)
                loadAllServices()
            } catch (_: Exception) {}
        }
    }
}
