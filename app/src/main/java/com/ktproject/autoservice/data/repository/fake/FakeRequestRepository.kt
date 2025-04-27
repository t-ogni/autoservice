package com.ktproject.autoservice.data.repository.fake

import com.ktproject.autoservice.data.model.User
import com.ktproject.autoservice.data.model.LoginRequest
import com.ktproject.autoservice.data.model.TokenResponse
import com.ktproject.autoservice.data.repository.UserRepository
import com.ktproject.autoservice.data.remote.ApiClient
import kotlinx.coroutines.delay
import kotlin.random.Random

class FakeRequestRepository(private val apiClient: ApiClient) : UserRepository {

    private val users = mutableListOf<User>(
        User("1", "user1@example.com", "User One", "admin"),
        User("2", "user2@example.com", "User Two", "user"),
        // Добавим еще несколько пользователей
    )

    override suspend fun getAllUsers(): List<User> {
        delay(500)
        return users
    }

    override suspend fun getUserById(userId: String): User? {
        delay(1000)
        return users.find { it.id == userId }
    }

    override suspend fun addUser(name: String, email: String, role: String) {
        delay(300)
        users.add(User(Random(System.currentTimeMillis()).nextInt(1000, 9999).toString(), name, email, role))
    }

    override suspend fun updateUser(userId: String, name: String?, email: String?) {
        name?.let { users.find { it.id == userId }?.name = it }
        email?.let { users.find { it.id == userId }?.email = it }
    }

    override suspend fun deleteUser(userId: String) {
        delay(300)
        users.removeIf { it.id == userId }
    }

    // Метод для выполнения логина
    override suspend fun login(request: LoginRequest): TokenResponse {
        delay(500)
        // Имитируем успешный логин и возврат токена
        if ("test" in request.email) {
            return TokenResponse("fake-token-12345")
        } else {
            throw NumberFormatException("ParseErr")
        }
    }
}
