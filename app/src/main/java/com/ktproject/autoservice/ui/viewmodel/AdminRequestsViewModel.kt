package com.ktproject.autoservice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.model.Request
import com.ktproject.autoservice.data.repository.RequestRepository
import com.ktproject.autoservice.ui.components.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminRequestsViewModel(
    private val requestRepository: RequestRepository
) : ViewModel() {

    private val _requestsState = MutableStateFlow<UIState<List<Request>>>(UIState.Loading)
    val requestsState: StateFlow<UIState<List<Request>>> = _requestsState

    private val _selectedRequest = MutableStateFlow<Request?>(null)
    val selectedRequest: StateFlow<Request?> = _selectedRequest

    fun loadAllRequests() {
        viewModelScope.launch {
            _requestsState.value = UIState.Loading
            try {
                val list = requestRepository.getAllRequests()
                _requestsState.value = UIState.Success(list)
            } catch (e: Exception) {
                _requestsState.value = UIState.Error(e.message ?: "Ошибка")
            }
        }
    }

    fun loadRequestById(id: String) {
        viewModelScope.launch {
            _selectedRequest.value = requestRepository.getAllRequests().find { it.id == id }
        }
    }

    fun updateRequestStatus(id: String, status: String, result: String?) {
        viewModelScope.launch {
            requestRepository.updateRequestStatus(id, status, result)
            loadAllRequests()
        }
    }
}
