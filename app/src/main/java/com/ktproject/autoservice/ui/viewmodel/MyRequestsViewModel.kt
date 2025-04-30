package com.ktproject.autoservice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.model.Request
import com.ktproject.autoservice.data.repository.RequestRepository
import com.ktproject.autoservice.ui.components.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyRequestsViewModel(
    private val requestRepository: RequestRepository
) : ViewModel() {

    private val _requestsState = MutableStateFlow<UIState<List<Request>>>(UIState.Loading)
    val requestsState: StateFlow<UIState<List<Request>>> = _requestsState

    init {
        loadMyRequests()
    }

    fun loadMyRequests() {
        viewModelScope.launch {
            try {
                _requestsState.value = UIState.Loading
                val requests = requestRepository.getMyRequests()
                _requestsState.value = UIState.Success(requests)
            } catch (e: Exception) {
                _requestsState.value = UIState.Error(e.message ?: "Ошибка загрузки заявок")
            }
        }
    }
}
