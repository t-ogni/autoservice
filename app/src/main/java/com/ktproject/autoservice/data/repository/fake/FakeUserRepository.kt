package com.ktproject.autoservice.data.repository.fake

import com.ktproject.autoservice.data.model.User
import com.ktproject.autoservice.data.repository.UserRepository
import com.ktproject.autoservice.data.remote.ApiClient
import kotlinx.coroutines.delay
import kotlin.random.Random

class FakeUserRepository(
    private val apiClient: ApiClient
) : UserRepository {

    private val users = mutableListOf<User>(
        User("1", "user1@example.com", "User One", "admin"),
        User("2", "user2@example.com", "User Two", "user"),
        // Добавим еще несколько пользователей
    )

    private var currentUserId: String? = null

    override suspend fun getCurrentUserId(): User? {
        return users.find { it.id == currentUserId }
    }

    fun setCurrentUser(userId: String) {
        currentUserId = userId
    }

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
        delay(300)
        name?.let { users.find { it.id == userId }?.name = it }
        email?.let { users.find { it.id == userId }?.email = it }
    }

    override suspend fun deleteUser(userId: String) {
        delay(300)
        users.removeIf { it.id == userId }
    }

    override suspend fun login(email: String, password: String): Boolean {
        delay(300)
        val user = users.find { it.email == email }
        return if (user != null && password == "password") {
            currentUserId = user.id
            apiClient.updateToken("fake-token-${user.id}")
            true
        } else {
            false
        }
    }

    override suspend fun register(name: String, email: String, password: String): Boolean {
        if (users.any { it.email == email }) return false
        val newUser = User(Random.nextInt(1000, 9999).toString(), email, name, "user")
        users.add(newUser)
        return true
    }

    override suspend fun logout() {
        currentUserId = null
        apiClient.clearToken()
    }

    override suspend fun isAuthenticated(): Boolean {
        val token = apiClient.getToken()
        return !token.isNullOrEmpty()
    }
}
