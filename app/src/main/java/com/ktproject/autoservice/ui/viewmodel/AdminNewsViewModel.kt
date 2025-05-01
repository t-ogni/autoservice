package com.ktproject.autoservice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.model.News
import com.ktproject.autoservice.data.repository.NewsRepository
import com.ktproject.autoservice.data.repository.ResultState
import com.ktproject.autoservice.ui.components.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminNewsViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _newsListState = MutableStateFlow<UIState<List<News>>>(UIState.Loading)
    val newsListState: StateFlow<UIState<List<News>>> = _newsListState.asStateFlow()

    private val _selectedNewsState = MutableStateFlow<UIState<News>>(UIState.Loading)
    val selectedNewsState: StateFlow<UIState<News>> = _selectedNewsState.asStateFlow()

    fun loadNews() {
        viewModelScope.launch {
            _newsListState.value = UIState.Loading
            when (val result = newsRepository.getNews()) {
                is ResultState.Success -> _newsListState.value = UIState.Success(result.data.sortedByDescending { it.date })
                is ResultState.Error -> _newsListState.value = UIState.Error(result.message)
                ResultState.Loading -> {} // игнорируем, т.к. уже загрузка
            }
        }
    }

    fun getNewsById(newsId: String) {
        viewModelScope.launch {
            _selectedNewsState.value = UIState.Loading
            when (val result = newsRepository.getNewsById(newsId)) {
                is ResultState.Success -> _selectedNewsState.value = UIState.Success(result.data)
                is ResultState.Error -> _selectedNewsState.value = UIState.Error(result.message)
                ResultState.Loading -> {}
            }
        }
    }

    fun addNews(
        title: String,
        content: String,
        date: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            when (val result = newsRepository.addNews(title, content, date)) {
                is ResultState.Success -> {
                    loadNews()
                    onSuccess()
                }
                is ResultState.Error -> onError(result.message)
                ResultState.Loading -> {}
            }
        }
    }

    fun updateNews(
        newsId: String,
        title: String,
        content: String,
        date: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            when (val result = newsRepository.updateNews(newsId, title, content, date)) {
                is ResultState.Success -> {
                    loadNews()
                    onSuccess()
                }
                is ResultState.Error -> onError(result.message)
                ResultState.Loading -> {}
            }
        }
    }

    fun deleteNews(
        newsId: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            when (val result = newsRepository.deleteNews(newsId)) {
                is ResultState.Success -> {
                    loadNews()
                    onSuccess()
                }
                is ResultState.Error -> onError(result.message)
                ResultState.Loading -> {}
            }
        }
    }
}
