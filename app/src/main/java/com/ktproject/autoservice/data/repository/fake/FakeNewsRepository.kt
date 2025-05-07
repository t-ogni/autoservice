package com.ktproject.autoservice.data.repository.fake

import com.ktproject.autoservice.data.model.News
import com.ktproject.autoservice.data.remote.ApiClient
import com.ktproject.autoservice.data.repository.NewsRepository
import kotlinx.coroutines.delay
import com.ktproject.autoservice.data.repository.ResultState

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

        News("2", "Новая услуга: Чистка кондиционеров", "Поддерживайте чистоту воздуха в салоне.", "2025-04-10"),
        News("3", "Акция на замену масла", "При замене масла – скидка на фильтр!", "2025-04-15"),
        News("4", "Розыгрыш среди клиентов", "Выиграй бесплатную диагностику!", "2025-04-20"),
        News("5", "Праздничные выходные", "Работаем в праздничные дни по графику.", "2025-05-01")
    )

    override suspend fun getNews(): ResultState<List<News>> {
        delay(300)
        return try {
            ResultState.Success(news)
        } catch (e: Exception) {
            ResultState.Error("Ошибка загрузки новостей: ${e.message}")
        }
    }

    override suspend fun getNewsById(newsId: String): ResultState<News> {
        delay(200)
        return try {
            val found = news.firstOrNull { it.id == newsId }
                ?: return ResultState.Error("Новость не найдена")
            ResultState.Success(found)
        } catch (e: Exception) {
            ResultState.Error("Ошибка получения новости: ${e.message}")
        }
    }

    override suspend fun addNews(title: String, content: String, date: String): ResultState<Unit> {
        delay(400)
        return try {
            val newId = ((news.maxByOrNull { it.id.toInt() }?.id?.toIntOrNull() ?: (0 + 1))).toString()
            val newNews = News(newId, title, content, date)
            news.add(newNews)
            ResultState.Success(Unit)
        } catch (e: Exception) {
            ResultState.Error("Ошибка добавления новости: ${e.message}")
        }
    }

    override suspend fun updateNews(newsId: String, title: String, content: String, date: String): ResultState<Unit> {
        delay(400)
        return try {
            val index = news.indexOfFirst { it.id == newsId }
            if (index != -1) {
                news[index] = News(newsId, title, content, date)
                ResultState.Success(Unit)
            } else {
                ResultState.Error("Новость для обновления не найдена")
            }
        } catch (e: Exception) {
            ResultState.Error("Ошибка обновления новости: ${e.message}")
        }
    }

    override suspend fun deleteNews(newsId: String): ResultState<Unit> {
        delay(300)
        return try {
            val deleted = news.removeIf { it.id == newsId }
            if (deleted) {
                ResultState.Success(Unit)
            } else {
                ResultState.Error("Новость для удаления не найдена")
            }
        } catch (e: Exception) {
            ResultState.Error("Ошибка удаления новости: ${e.message}")
        }
    }
}
