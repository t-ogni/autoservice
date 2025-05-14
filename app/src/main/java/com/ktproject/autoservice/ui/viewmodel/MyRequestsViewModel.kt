package com.ktproject.autoservice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.model.Request
import com.ktproject.autoservice.data.repository.RepositoryResult
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
            _requestsState.value = UIState.Loading
            when (val result = requestRepository.getMyRequests()) {
                is RepositoryResult.Success -> _requestsState.value = UIState.Success(result.data)
                is RepositoryResult.Error -> _requestsState.value = UIState.Error(result.message)
                is RepositoryResult.NetworkError -> _requestsState.value = UIState.Error(result.message)
            }
        }
    }
}
