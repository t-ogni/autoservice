package com.ktproject.autoservice.data.repository

import com.ktproject.autoservice.data.model.News
import com.ktproject.autoservice.data.model.Request
import com.ktproject.autoservice.data.model.Service
import com.ktproject.autoservice.data.model.User

interface UserRepository {
    suspend fun getAllUsers(): List<User>
    suspend fun getUserById(userId: String): User?
    suspend fun addUser(name: String, email: String, role: String)
    suspend fun updateUser(userId: String, name: String?, email: String?, role: String?)
    suspend fun deleteUser(userId: String)
    suspend fun getCurrentUser(): User?
    suspend fun isAuthenticated(): Boolean
    suspend fun login(email: String, password: String): ResultState<Unit>
    suspend fun register(name: String, email: String, password: String): ResultState<Unit>
    suspend fun logout()
}

interface ServiceRepository {
    suspend fun getAllServices(): List<Service>
    suspend fun getServiceById(serviceId: String): Service?
    suspend fun addService(name: String, description: String, price: String)
    suspend fun deleteService(serviceId: String)
}

interface RequestRepository {
    suspend fun createRequest(serviceId: String, description: String): String
    suspend fun createRequest(
        serviceId: String,
        description: String,
        date: String,
        time: String
    ): String

    suspend fun getMyRequests(): List<Request>
    suspend fun getAllRequests(): List<Request>
    suspend fun updateRequestStatus(requestId: String, status: String, result: String?)
}

interface NewsRepository {
    suspend fun getNews(): ResultState<List<News>>
    suspend fun getNewsById(newsId: String): ResultState<News>
    suspend fun addNews(title: String, content: String, date: String): ResultState<Unit>
    suspend fun updateNews(newsId: String, title: String, content: String, date: String): ResultState<Unit>
    suspend fun deleteNews(newsId: String): ResultState<Unit>
}
