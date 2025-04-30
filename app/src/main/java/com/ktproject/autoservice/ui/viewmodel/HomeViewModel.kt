package com.ktproject.autoservice.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.model.News
import com.ktproject.autoservice.data.model.Request
import com.ktproject.autoservice.data.repository.NewsRepository
import com.ktproject.autoservice.data.repository.RequestRepository
import com.ktproject.autoservice.data.repository.UserRepository
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
    val newsList: List<News> = emptyList()
)

class HomeViewModel(
    private val userRepository: UserRepository,
    private val newsRepository: NewsRepository,
    private val requestRepository: RequestRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiStateData())
    val uiState: StateFlow<HomeUiStateData> = _uiState

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = HomeUiStateData(
                homeUiState = HomeUiState.Loading
            )

            val role = userRepository.getUserById("1")?.role ?: "user"
            val requests = requestRepository.getMyRequests()
            val news = newsRepository.getNews()

            _uiState.value = HomeUiStateData(
                homeUiState = HomeUiState.Idle,
                userRole = role,
                myRequests = requests,
                newsList = news
            )
        }
    }
}
