package com.ktproject.autoservice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.model.News
import com.ktproject.autoservice.data.repository.NewsRepository
import com.ktproject.autoservice.data.repository.RepositoryResult
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
                is RepositoryResult.Success -> _newsListState.value = UIState.Success(result.data.sortedByDescending { it.date })
                is RepositoryResult.Error -> _newsListState.value = UIState.Error(result.message)
                is RepositoryResult.NetworkError -> _newsListState.value = UIState.Error(result.message)
            }
        }
    }

    fun getNewsById(newsId: String) {
        viewModelScope.launch {
            _selectedNewsState.value = UIState.Loading
            when (val result = newsRepository.getNewsById(newsId)) {
                is RepositoryResult.Success -> _selectedNewsState.value = UIState.Success(result.data)
                is RepositoryResult.Error -> _selectedNewsState.value = UIState.Error(result.message)
                is RepositoryResult.NetworkError -> _newsListState.value = UIState.Error(result.message)
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
                is RepositoryResult.Success -> {
                    loadNews()
                    onSuccess()
                }
                is RepositoryResult.Error -> onError(result.message)
                is RepositoryResult.NetworkError -> _newsListState.value = UIState.Error(result.message)
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
                is RepositoryResult.Success -> {
                    loadNews()
                    onSuccess()
                }
                is RepositoryResult.Error -> onError(result.message)
                is RepositoryResult.NetworkError -> _newsListState.value = UIState.Error(result.message)
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
                is RepositoryResult.Success -> {
                    loadNews()
                    onSuccess()
                }
                is RepositoryResult.Error -> onError(result.message)
                is RepositoryResult.NetworkError -> _newsListState.value = UIState.Error(result.message)
            }
        }
    }
}
