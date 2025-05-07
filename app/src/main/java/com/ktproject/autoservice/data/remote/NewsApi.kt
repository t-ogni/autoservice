package com.ktproject.autoservice.data.remote

import com.ktproject.autoservice.data.model.AddNewsRequest
import com.ktproject.autoservice.data.model.ApiResponse
import com.ktproject.autoservice.data.model.News
import com.ktproject.autoservice.data.model.UpdateNewsRequest

class NewsApi(private val apiClient: ApiClient) {

    suspend fun getNews(): ApiResponse<List<News>> {
        return apiClient.safeGet("/news")
    }

    suspend fun getNewsById(id: String): ApiResponse<News> {
        return apiClient.safeGet("/news/$id")
    }

    suspend fun addNews(request: AddNewsRequest): ApiResponse<Unit> {
        return apiClient.safePost("/news", request)
    }

    suspend fun updateNews(id: String, request: UpdateNewsRequest): ApiResponse<Unit> {
        return apiClient.safePut("/news/$id", request)
    }

    suspend fun deleteNews(id: String): ApiResponse<Unit> {
        return apiClient.safeDelete("/news/$id")
    }
}
