package com.ktproject.autoservice.data.repository.fake

import com.ktproject.autoservice.data.model.News
import com.ktproject.autoservice.data.remote.ApiClient
import com.ktproject.autoservice.data.repository.NewsRepository
import com.ktproject.autoservice.data.repository.RepositoryResult
import kotlinx.coroutines.delay
import kotlin.random.Random

class FakeNewsRepository(
    private val apiClient: ApiClient
) : NewsRepository {

    private val news = mutableListOf(
        News("1", "Скидка 20% на ТО", "<h2>Новое открытие</h2>\n" +
                "<p>Мы рады сообщить об открытии нового филиала!</p>\n" +
                "<ul>\n" +
                "  <li>Удобное расположение</li>\n" +
                "  <li>Квалифицированный персонал</li>\n" +
                "</ul>\n", "2025-04-01"),

        News("22", "Новая услуга: Чистка кондиционеров", "Поддерживайте чистоту воздуха в салоне.", "2025-04-10"),

        News("23", "Новая услуга: Чистка кондиционеров", "Поддерживайте чистоту воздуха в салоне.", "2025-04-10"),

        News("25", "Новая услуга: Чистка кондиционеров", "Поддерживайте чистоту воздуха в салоне.", "2025-04-10"),

        News("39", "Новая услуга: фывфывфывфывфыафыафыафыафыафыа кондиционеров", "Поддерживайте чистоту воздуха в салоне.", "2025-04-10"),

        News("29", "Новая услуга: Чистка кондиционеров", "Поддерживайте чистоту воздуха в салоне.", "2025-04-10"),

        News("2", "Новая услуга: Чистка кондиционеров", "Поддерживайте чистоту воздуха в салоне.", "2025-04-10"),
        News("3", "Акция на замену масла", "При замене масла – скидка на фильтр!", "2025-04-15"),
        News("4", "Розыгрыш среди клиентов", "Выиграй бесплатную диагностику!", "2025-04-20"),
        News("5", "Праздничные выходные", "Работаем в праздничные дни по графику.", "2025-05-01")
    )

    private fun shouldFail() = Random.nextFloat() < 0.6f

    override suspend fun getNews(): RepositoryResult<List<News>> {
        delay(300)
        return if (shouldFail()) {
            RepositoryResult.Error("Ошибка загрузки новостей (симуляция сбоя)")
        } else {
            RepositoryResult.Success(news)
        }
    }

    override suspend fun getNewsById(newsId: String): RepositoryResult<News> {
        delay(200)
        return if (shouldFail()) {
            RepositoryResult.Error("Ошибка получения новости (симуляция сбоя)")
        } else {
            val found = news.firstOrNull { it.id == newsId }
            if (found != null) RepositoryResult.Success(found)
            else RepositoryResult.Error("Новость не найдена")
        }
    }

    override suspend fun addNews(title: String, content: String, date: String): RepositoryResult<Unit> {
        delay(400)
        return if (shouldFail()) {
            RepositoryResult.Error("Ошибка добавления новости (симуляция сбоя)")
        } else {
            val newId = ((news.maxByOrNull { it.id.toInt() }?.id?.toIntOrNull() ?: 0) + 1).toString()
            val newNews = News(newId, title, content, date)
            news.add(newNews)
            RepositoryResult.Success(Unit)
        }
    }

    override suspend fun updateNews(newsId: String, title: String, content: String, date: String): RepositoryResult<Unit> {
        delay(400)
        return if (shouldFail()) {
            RepositoryResult.Error("Ошибка обновления новости (симуляция сбоя)")
        } else {
            val index = news.indexOfFirst { it.id == newsId }
            if (index != -1) {
                news[index] = News(newsId, title, content, date)
                RepositoryResult.Success(Unit)
            } else {
                RepositoryResult.Error("Новость для обновления не найдена")
            }
        }
    }

    override suspend fun deleteNews(newsId: String): RepositoryResult<Unit> {
        delay(300)
        return if (shouldFail()) {
            RepositoryResult.Error("Ошибка удаления новости (симуляция сбоя)")
        } else {
            val deleted = news.removeIf { it.id == newsId }
            if (deleted) RepositoryResult.Success(Unit)
            else RepositoryResult.Error("Новость для удаления не найдена")
        }
    }
}
