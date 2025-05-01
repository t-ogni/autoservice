package com.ktproject.autoservice.data.repository.fake

import com.ktproject.autoservice.data.model.User
import com.ktproject.autoservice.data.repository.UserRepository
import com.ktproject.autoservice.data.remote.ApiClient
import com.ktproject.autoservice.data.repository.ResultState
import kotlinx.coroutines.delay
import kotlin.random.Random

class FakeUserRepository(
    private val apiClient: ApiClient
) : UserRepository {

    private val users = mutableListOf<User>(
        User("1", "User One", "user1@gmail.com", "admin"),
        User("2", "User Two", "user2@gmail.com", "user")
    )

    private var currentUserId: String? = null

    override suspend fun getCurrentUser(): User? {
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

    override suspend fun updateUser(userId: String, name: String?, email: String?, role: String?) {
        delay(300)
        users.find { it.id == userId }?.let { user ->
            name?.let { user.name = name }
            email?.let { user.email = email }
            role?.let { user.role = role }
        }
    }

    override suspend fun deleteUser(userId: String) {
        delay(300)
        users.removeIf { it.id == userId }
    }

    override suspend fun login(email: String, password: String): ResultState<Unit> {
        delay(300)
        val user = users.find { it.email == email }
        if (user != null) {
            if (password == "password") {
                currentUserId = user.id
                apiClient.updateToken("fake-token-${user.id}")
                return ResultState.Success(Unit)
            } else {
                return ResultState.Error("Пароль неверный")
            }
        } else {
            return ResultState.Error("Email не найден")
        }
    }

    override suspend fun register(name: String, email: String, password: String): ResultState<Unit> {
        if (users.any { it.email == email })
            return ResultState.Error("Email уже используется")

        val newUser = User(Random.nextInt(1000, 9999).toString(), email, name, "user")
        users.add(newUser)
        return ResultState.Success(Unit)
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
