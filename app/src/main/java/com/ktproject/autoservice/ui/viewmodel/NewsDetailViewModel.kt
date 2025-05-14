package com.ktproject.autoservice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.model.News
import com.ktproject.autoservice.data.repository.NewsRepository
import com.ktproject.autoservice.data.repository.RepositoryResult
import com.ktproject.autoservice.ui.components.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsDetailViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _newsState = MutableStateFlow<UIState<News>>(UIState.Loading)
    val newsState: StateFlow<UIState<News>> = _newsState

    fun loadNews(newsId: String) {
        viewModelScope.launch {
            _newsState.value = UIState.Loading

            when (val result = newsRepository.getNewsById(newsId)) {
                is RepositoryResult.Success -> {
                    _newsState.value = UIState.Success(result.data)
                }
                is RepositoryResult.Error -> {
                    _newsState.value = UIState.Error(result.message)
                }
                is RepositoryResult.NetworkError -> {
                    _newsState.value = UIState.Error(result.message)
                }
            }
        }
    }
}
