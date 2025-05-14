package com.ktproject.autoservice.data.repository.impl

import com.ktproject.autoservice.api.responses.ApiResponse
import io.ktor.client.statement.bodyAsText
import com.ktproject.autoservice.data.model.*
import com.ktproject.autoservice.data.remote.*
import com.ktproject.autoservice.data.repository.*
import com.ktproject.autoservice.data.repository.RepositoryResult.*
import io.ktor.client.call.body
import io.ktor.client.plugins.*
import io.ktor.http.*
import io.ktor.util.reflect.TypeInfo
import kotlinx.serialization.json.Json

class UserRepositoryImpl(private val apiClient: ApiClient) : UserRepository {

    override suspend fun getAllUsers(): RepositoryResult<List<User>> = safeCall {
        apiClient.get("/users")
    }

    override suspend fun getUserById(userId: String): RepositoryResult<User> = safeCall {
        apiClient.get("/users/$userId")
    }

    override suspend fun addUser(name: String, email: String, role: String): RepositoryResult<Unit> = safeCall {
        apiClient.post("/users", mapOf("name" to name, "email" to email, "role" to role))
    }

    override suspend fun updateUser(userId: String, name: String?, email: String?, role: String?): RepositoryResult<Unit> = safeCall {
        apiClient.put("/users/$userId", mapOf("name" to name, "email" to email, "role" to role))
    }

    override suspend fun deleteUser(userId: String): RepositoryResult<Unit> = safeCall {
        apiClient.delete("/users/$userId")
    }

    override suspend fun getCurrentUser(): RepositoryResult<User> = safeCall {
        apiClient.get("/users/me", typeInfo<User>())
    }

    override suspend fun isAuthenticated(): RepositoryResult<Boolean> = safeCall {
        val token = apiClient.getToken()
        Success(!token.isNullOrEmpty())
    }

    override suspend fun login(email: String, password: String): RepositoryResult<Unit> = safeCall {
        apiClient.post("/login", mapOf("email" to email, "password" to password))
    }

    override suspend fun register(name: String, email: String, password: String): RepositoryResult<Unit> = safeCall {
        apiClient.post("/register", mapOf("name" to name, "email" to email, "password" to password))
    }

    override suspend fun logout(): RepositoryResult<Unit> = safeCall {
        apiClient.post("/logout")
    }
}

class ServiceRepositoryImpl(private val apiClient: ApiClient) : ServiceRepository {

    override suspend fun getAllServices(): RepositoryResult<List<Service>> = safeCall {
        apiClient.get("/services", typeInfo<List<Service>>())
    }

    override suspend fun getServiceById(serviceId: String): RepositoryResult<Service> = safeCall {
        apiClient.get("/services/$serviceId", typeInfo<Service>())
    }

    override suspend fun addService(name: String, description: String, price: String): RepositoryResult<Unit> = safeCall {
        apiClient.post("/services", mapOf("name" to name, "description" to description, "price" to price))
    }

    override suspend fun deleteService(serviceId: String): RepositoryResult<Unit> = safeCall {
        apiClient.delete("/services/$serviceId")
    }
}

class RequestRepositoryImpl(private val apiClient: ApiClient) : RequestRepository {

    override suspend fun createRequest(serviceId: String, description: String): RepositoryResult<String> = safeCall {
        apiClient.post("/requests", mapOf("serviceId" to serviceId, "description" to description))
    }

    override suspend fun createRequest(serviceId: String, description: String, date: String, time: String): RepositoryResult<String> = safeCall {
        apiClient.post("/requests", mapOf("serviceId" to serviceId, "description" to description, "date" to date, "time" to time))
    }

    override suspend fun getMyRequests(): RepositoryResult<List<Request>> = safeCall {
        apiClient.get("/my_requests", typeInfo<List<Request>>())
    }

    override suspend fun getAllRequests(): RepositoryResult<List<Request>> = safeCall {
        apiClient.get("/requests", typeInfo<List<Request>>())
    }

    override suspend fun updateRequestStatus(requestId: String, status: String, result: String?): RepositoryResult<Unit> = safeCall {
        apiClient.put("/requests/$requestId", mapOf("status" to status, "result" to result))
    }
}

class NewsRepositoryImpl(private val apiClient: ApiClient) : NewsRepository {

    override suspend fun getNews(): RepositoryResult<List<News>> = safeCall {
        apiClient.get("/news", typeInfo<List<News>>())
    }

    override suspend fun getNewsById(newsId: String): RepositoryResult<News> = safeCall {
        apiClient.get("/news/$newsId", typeInfo<News>())
    }

    override suspend fun addNews(title: String, content: String, date: String): RepositoryResult<Unit> = safeCall {
        apiClient.post("/news", mapOf("title" to title, "content" to content, "date" to date))
    }

    override suspend fun updateNews(newsId: String, title: String, content: String, date: String): RepositoryResult<Unit> = safeCall {
        apiClient.put("/news/$newsId", typeInfo = mapOf("title" to title, "content" to content, "date" to date))
    }

    override suspend fun deleteNews(newsId: String): RepositoryResult<Unit> = safeCall {
        apiClient.delete("/news/$newsId")
    }
}

suspend inline fun <reified T : Any> safeCall(block: () -> ApiResponse<T>): RepositoryResult<T> {
    return try {
        val response = block()
        if (response.success) {
            if (response.data != null) {
                RepositoryResult.Success(response.data)
            } else {
                RepositoryResult.Error("Пустой ответ от сервера")
            }
        } else {
            RepositoryResult.Error(response.error ?: "Неизвестная ошибка")
        }
    } catch (e: Exception) {
        RepositoryResult.NetworkError
    }
}

