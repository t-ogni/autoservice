package com.ktproject.autoservice.data.repository.fake

import com.ktproject.autoservice.data.model.News
import com.ktproject.autoservice.data.remote.ApiClient
import com.ktproject.autoservice.data.repository.NewsRepository
import kotlinx.coroutines.delay

class FakeNewsRepository(private val apiClient: ApiClient) : NewsRepository {

    private val news = mutableListOf(
        News("1", "Скидка 20% на ТО", "Только в мае! Пройдите ТО со скидкой.", "2025-04-01"),
        News("2", "Новая услуга: Чистка кондиционеров", "Поддерживайте чистоту воздуха в салоне.", "2025-04-10"),
        News("3", "Акция на замену масла", "При замене масла – скидка на фильтр!", "2025-04-15"),
        News("4", "Розыгрыш среди клиентов", "Выиграй бесплатную диагностику!", "2025-04-20"),
        News("5", "Праздничные выходные", "Работаем в праздничные дни по графику.", "2025-05-01")
    )

    override suspend fun getNews(): List<News> {
        delay(300)
        return news
    }

    override suspend fun getNewsById(id: Int): News {
        delay(200)
        return news.firstOrNull { it.id == id.toString() }
            ?: throw IllegalArgumentException("Новость не найдена")
    }

    override suspend fun addNews(title: String, content: String, date: String) {
        delay(400)
        val newId = (news.size + 1).toString()
        val newNews = News(newId, title, content, date)
        news.add(newNews)
    }

    override suspend fun updateNews(id: Int, title: String, content: String, date: String) {
        delay(400)
        val index = news.indexOfFirst { it.id == id.toString() }
        if (index != -1) {
            news[index] = News(id.toString(), title, content, date)
        } else {
            throw IllegalArgumentException("Новость для обновления не найдена")
        }
    }

    override suspend fun deleteNews(id: Int) {
        delay(300)
        val deleted = news.removeIf { it.id == id.toString() }
        if (!deleted) {
            throw IllegalArgumentException("Новость для удаления не найдена")
        }
    }
}
