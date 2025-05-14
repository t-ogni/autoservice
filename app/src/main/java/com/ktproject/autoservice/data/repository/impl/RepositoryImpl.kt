package com.ktproject.autoservice.data.repository.impl

import com.ktproject.autoservice.api.requests.AddNewsRequest
import com.ktproject.autoservice.api.requests.CreateRequestRequest
import com.ktproject.autoservice.api.requests.LogoutRequest
import com.ktproject.autoservice.api.requests.UpdateNewsRequest
import com.ktproject.autoservice.api.responses.ApiResponse
import com.ktproject.autoservice.component.carList.CarModel
import com.ktproject.autoservice.data.model.*
import com.ktproject.autoservice.data.remote.*
import com.ktproject.autoservice.data.repository.*
import com.ktproject.autoservice.data.repository.RepositoryResult.*

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
        apiClient.get("/users/me")
    }

    override suspend fun isAuthenticated(): RepositoryResult<Boolean>  {
        val token = apiClient.getToken()
        return Success(!token.isNullOrEmpty())
    }

    override suspend fun login(email: String, password: String): RepositoryResult<Unit> = safeCall {
        apiClient.post("/login", mapOf("email" to email, "password" to password))
    }

    override suspend fun register(name: String, email: String, password: String): RepositoryResult<Unit> = safeCall {
        apiClient.post("/register", mapOf("name" to name, "email" to email, "password" to password))
    }

    override suspend fun logout(): RepositoryResult<Unit> = safeCall {
        apiClient.post("/logout", LogoutRequest(apiClient.getToken() ?: ""))
    }
}

class ServiceRepositoryImpl(private val apiClient: ApiClient) : ServiceRepository {

    override suspend fun getAllServices(): RepositoryResult<List<Service>> = safeCall {
        apiClient.get("/services")
    }

    override suspend fun getServiceById(serviceId: String): RepositoryResult<Service> = safeCall {
        apiClient.get("/services/$serviceId")
    }

    override suspend fun addService(name: String, description: String, price: String): RepositoryResult<Unit> = safeCall {
        apiClient.post("/services", mapOf("name" to name, "description" to description, "price" to price))
    }

    override suspend fun deleteService(serviceId: String): RepositoryResult<Unit> = safeCall {
        apiClient.delete("/services/$serviceId")
    }
}

class RequestRepositoryImpl(private val apiClient: ApiClient) : RequestRepository {
    override suspend fun createRequest(
        serviceId: String,
        description: String,
        carBrand: String,
        carModel: String
    ): RepositoryResult<String> = safeCall {
        apiClient.post("/requests", CreateRequestRequest(serviceId, description, "2000-01-01", "10:00", carModel, carBrand))
    }

    override suspend fun createRequest(serviceId: String, description: String, date: String, time: String, carBrand: String, carModel: String): RepositoryResult<String> = safeCall {
        apiClient.post("/requests", CreateRequestRequest(serviceId, description, date, time, carModel, carBrand))
    }

    override suspend fun getMyRequests(): RepositoryResult<List<Request>> = safeCall {
        apiClient.get("/my_requests")
    }

    override suspend fun getAllRequests(): RepositoryResult<List<Request>> = safeCall {
        apiClient.get("/requests")
    }

    override suspend fun updateRequestStatus(requestId: String, status: String, result: String?): RepositoryResult<Unit> = safeCall {
        apiClient.put("/requests/$requestId", mapOf("status" to status, "result" to result))
    }
}

class NewsRepositoryImpl(private val apiClient: ApiClient) : NewsRepository {

    override suspend fun getNews(): RepositoryResult<List<News>> = safeCall {
        apiClient.get("/news")
    }

    override suspend fun getNewsById(newsId: String): RepositoryResult<News> = safeCall {
        apiClient.get("/news/$newsId")
    }

    override suspend fun addNews(title: String, content: String, date: String): RepositoryResult<Unit> = safeCall {
        apiClient.post("/news", AddNewsRequest(title, content, date))
    }

    override suspend fun updateNews(newsId: String, title: String, content: String, date: String): RepositoryResult<Unit> = safeCall {
        apiClient.put("/news/$newsId", UpdateNewsRequest(title, content, date))
    }

    override suspend fun deleteNews(newsId: String): RepositoryResult<Unit> = safeCall {
        apiClient.delete("/news/$newsId")
    }
}

inline fun <reified T : Any> safeCall(block: () -> ApiResponse<T>): RepositoryResult<T> {
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
        RepositoryResult.NetworkError()
    }
}

