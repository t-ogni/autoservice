package com.ktproject.autoservice.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.model.News
import com.ktproject.autoservice.data.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    // Состояние для списка новостей
    private val _newsList = MutableStateFlow<List<News>>(emptyList())
    val newsList: StateFlow<List<News>> get() = _newsList

    // Функция для загрузки всех новостей
    fun loadNews() {
        viewModelScope.launch {
            if (_newsList.value.isEmpty()) {
                _newsList.value = newsRepository.getNews()
            }
        }
    }


    fun getNewsByIdFlow(newsId: String): Flow<News?> = newsList.map { list ->
        list.find { it.id == newsId }
    }


    // Функция для добавления новости
    fun addNews(title: String, content: String, date: String) {
        viewModelScope.launch {
            newsRepository.addNews(title, content, date)
            loadNews()  // Обновляем список после добавления
        }
    }

    // Функция для редактирования новости
    fun updateNews(newsId: String, title: String, content: String, date: String) {
        viewModelScope.launch {
            newsRepository.updateNews(newsId, title, content, date)
            loadNews()  // Обновляем список после редактирования
        }
    }

    // Функция для удаления новости
    fun deleteNews(newsId: String) {
        viewModelScope.launch {
            newsRepository.deleteNews(newsId)
            loadNews()  // Обновляем список после удаления
        }
    }
}
