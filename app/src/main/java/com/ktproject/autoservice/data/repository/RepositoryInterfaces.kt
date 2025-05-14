package com.ktproject.autoservice.data.repository

import com.ktproject.autoservice.data.model.News
import com.ktproject.autoservice.data.model.Request
import com.ktproject.autoservice.data.model.Service
import com.ktproject.autoservice.data.model.User

interface UserRepository {
    suspend fun getAllUsers(): RepositoryResult<List<User>>
    suspend fun getUserById(userId: String): RepositoryResult<User>
    suspend fun addUser(name: String, email: String, role: String): RepositoryResult<Unit>
    suspend fun updateUser(userId: String, name: String?, email: String?, role: String?): RepositoryResult<Unit>
    suspend fun deleteUser(userId: String): RepositoryResult<Unit>
    suspend fun getCurrentUser(): RepositoryResult<User>
    suspend fun isAuthenticated(): RepositoryResult<Boolean>
    suspend fun login(email: String, password: String): RepositoryResult<Unit>
    suspend fun register(name: String, email: String, password: String): RepositoryResult<Unit>
    suspend fun logout(): RepositoryResult<Unit>
}

interface ServiceRepository {
    suspend fun getAllServices(): RepositoryResult<List<Service>>
    suspend fun getServiceById(serviceId: String): RepositoryResult<Service>
    suspend fun addService(name: String, description: String, price: String): RepositoryResult<Unit>
    suspend fun deleteService(serviceId: String): RepositoryResult<Unit>
}

interface RequestRepository {
    suspend fun createRequest(serviceId: String, description: String, carBrand: String = "", carModel: String = ""): RepositoryResult<String>
    suspend fun createRequest(serviceId: String, description: String, date: String, time: String, carBrand: String = "", carModel: String = ""): RepositoryResult<String>
    suspend fun getMyRequests(): RepositoryResult<List<Request>>
    suspend fun getAllRequests(): RepositoryResult<List<Request>>
    suspend fun updateRequestStatus(requestId: String, status: String, result: String?): RepositoryResult<Unit>
}

interface NewsRepository {
    suspend fun getNews(): RepositoryResult<List<News>>
    suspend fun getNewsById(newsId: String): RepositoryResult<News>
    suspend fun addNews(title: String, content: String, date: String): RepositoryResult<Unit>
    suspend fun updateNews(newsId: String, title: String, content: String, date: String): RepositoryResult<Unit>
    suspend fun deleteNews(newsId: String): RepositoryResult<Unit>
}
