package com.ktproject.autoservice.data.repository.fake

import com.ktproject.autoservice.data.model.User
import com.ktproject.autoservice.data.repository.UserRepository
import com.ktproject.autoservice.data.remote.ApiClient
import com.ktproject.autoservice.data.repository.RepositoryResult
import kotlinx.coroutines.delay
import kotlin.random.Random

class FakeUserRepository(
    private val apiClient: ApiClient
) : UserRepository {

    private val users = mutableListOf(
        User("1", "User One", "1", "admin"),
        User("2", "User Two", "user2@gmail.com", "user")
    )

    private val passwords = mutableListOf(
        "1" to "1",
        "2" to "1"
    )

    private var currentUserId: String? = null

    private fun shouldFail(): Boolean = Random.nextFloat() < 0.6

    override suspend fun getCurrentUser(): RepositoryResult<User> {
        val user = users.find { it.id == currentUserId }
        if (user != null) {
            return RepositoryResult.Success(user)
        } else {
            return RepositoryResult.Error("Пользователь не найден")
        }
    }

    fun setCurrentUser(userId: String) {
        currentUserId = userId
    }

    override suspend fun getAllUsers(): RepositoryResult<List<User>> {
        delay(500)
        return if (shouldFail()) {
            RepositoryResult.Error("Ошибка загрузки пользователей")
        } else {
            RepositoryResult.Success(users)
        }
    }

    override suspend fun getUserById(userId: String): RepositoryResult<User> {
        delay(1000)
        return if (shouldFail()) {
            RepositoryResult.Error("Ошибка загрузки пользователя")
        } else {
            users.find { it.id == userId }?.let {
                RepositoryResult.Success(it)
            } ?: RepositoryResult.Error("Пользователь не найден")
        }
    }

    override suspend fun addUser(name: String, email: String, role: String): RepositoryResult<Unit> {
        delay(300)
        return if (shouldFail()) {
            RepositoryResult.Error("Ошибка добавления пользователя")
        } else try {
            val newId = Random.nextInt(1000, 9999).toString()
            users.add(User(newId, name, email, role))
            RepositoryResult.Success(Unit)
        } catch (e: Exception) {
            RepositoryResult.Error("Ошибка: ${e.message}")
        }
    }

    override suspend fun updateUser(userId: String, name: String?, email: String?, role: String?): RepositoryResult<Unit> {
        delay(300)
        return if (shouldFail()) {
            RepositoryResult.Error("Ошибка обновления пользователя")
        } else {
            val user = users.find { it.id == userId }
            return if (user != null) {
                name?.let { user.name = it }
                email?.let { user.email = it }
                role?.let { user.role = it }
                RepositoryResult.Success(Unit)
            } else {
                RepositoryResult.Error("Пользователь не найден")
            }
        }
    }

    override suspend fun deleteUser(userId: String): RepositoryResult<Unit> {
        delay(300)
        return if (shouldFail()) {
            RepositoryResult.Error("Ошибка удаления пользователя")
        } else {
            val removed = users.removeIf { it.id == userId }
            if (removed) {
                RepositoryResult.Success(Unit)
            } else {
                RepositoryResult.Error("Пользователь не найден")
            }
        }
    }

    override suspend fun login(email: String, password: String): RepositoryResult<Unit> {
        delay(300)
        return if (shouldFail()) {
            RepositoryResult.Error("Ошибка входа")
        } else {
            val user = users.find { it.email == email }
            if (user != null) {
                val correctPassword = passwords.find { it.first == user.id }?.second
                return if (password == correctPassword) {
                    currentUserId = user.id
                    apiClient.updateToken("fake-token-${user.id}")
                    RepositoryResult.Success(Unit)
                } else {
                    RepositoryResult.Error("Пароль неверный")
                }
            } else {
                RepositoryResult.Error("Email не найден")
            }
        }
    }

    override suspend fun register(name: String, email: String, password: String): RepositoryResult<Unit> {
        delay(500)
        return if (shouldFail()) {
            RepositoryResult.Error("Ошибка регистрации")
        } else {
            if (users.any { it.email == email }) {
                RepositoryResult.Error("Email уже используется")
            } else {
                val newId = Random.nextInt(1000, 9999).toString()
                val newUser = User(newId, name, email, "user")
                users.add(newUser)
                passwords.add(newId to password)
                currentUserId = newId
                apiClient.updateToken("fake-token-${newId}")
                RepositoryResult.Success(Unit)
            }
        }
    }

    override suspend fun logout(): RepositoryResult<Unit> {
        return try {
            currentUserId = null
            apiClient.clearToken()
            RepositoryResult.Success(Unit)
        } catch (e: Exception) {
            RepositoryResult.Error("Ошибка выхода: ${e.message}")
        }
    }

    override suspend fun isAuthenticated(): RepositoryResult<Boolean> {
        return RepositoryResult.Success(!apiClient.getToken().isNullOrEmpty())
    }
}
