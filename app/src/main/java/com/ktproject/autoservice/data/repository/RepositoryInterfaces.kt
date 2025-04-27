package com.ktproject.autoservice.data.repository

import com.ktproject.autoservice.data.model.LoginRequest
import com.ktproject.autoservice.data.model.News
import com.ktproject.autoservice.data.model.Request
import com.ktproject.autoservice.data.model.Service
import com.ktproject.autoservice.data.model.TokenResponse
import com.ktproject.autoservice.data.model.User
import kotlinx.coroutines.delay


//interface AuthRepository {
//    suspend fun register(name: String, email: String, password: String): String
//    suspend fun login(email: String, password: String): String
//    suspend fun logout()
//}

interface UserRepository {
    suspend fun getAllUsers(): List<User>
    suspend fun getUserById(userId: String): User?
    suspend fun addUser(name: String, email: String, role: String)
    suspend fun updateUser(userId: String, name: String?, email: String?)
    suspend fun deleteUser(userId: String)
    suspend fun login(request: LoginRequest): TokenResponse
}

interface ServiceRepository {
    suspend fun getAllServices(): List<Service>
    suspend fun getServiceById(serviceId: String): Service?
    suspend fun addService(name: String, description: String, price: Int)
    suspend fun deleteService(serviceId: String)
}

interface RequestRepository {
    suspend fun createRequest(serviceId: Int, description: String): Int
    suspend fun getMyRequests(): List<Request>
    suspend fun getAllRequests(): List<Request>
    suspend fun updateRequestStatus(id: Int, status: String, result: String?)
}

interface NewsRepository {
    suspend fun getNews(): List<News>
    suspend fun getNewsById(id: Int): News
    suspend fun addNews(title: String, content: String, date: String)
    suspend fun updateNews(id: Int, title: String, content: String, date: String)
    suspend fun deleteNews(id: Int)
}
