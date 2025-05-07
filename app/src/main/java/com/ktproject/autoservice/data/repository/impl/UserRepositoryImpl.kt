package com.ktproject.autoservice.data.repository.impl

import com.ktproject.autoservice.data.model.AddUserRequest
import com.ktproject.autoservice.data.model.LoginRequest
import com.ktproject.autoservice.data.model.RegisterRequest
import com.ktproject.autoservice.data.model.UpdateUserRequest
import com.ktproject.autoservice.data.model.User
import com.ktproject.autoservice.data.repository.UserRepository
import com.ktproject.autoservice.data.remote.ApiClient
import com.ktproject.autoservice.data.remote.AuthApi
import com.ktproject.autoservice.data.remote.UserApi
import com.ktproject.autoservice.data.repository.ResultState
import kotlinx.coroutines.delay
import kotlin.random.Random


class UserRepositoryImpl(private val userApi: UserApi, private val authApi: AuthApi) : UserRepository {
    override suspend fun getAllUsers(): List<User> {
        return userApi.getAllUsers().data.orEmpty()
    }

    override suspend fun getUserById(userId: String): User? {
        return userApi.getUserById(userId).data
    }

    override suspend fun addUser(name: String, email: String, role: String) {
        userApi.addUser(AddUserRequest(name, email, role))
    }

    override suspend fun updateUser(userId: String, name: String?, email: String?, role: String?) {
        userApi.updateUser(userId, UpdateUserRequest(name, email, role))
    }

    override suspend fun deleteUser(userId: String) {
        userApi.deleteUser(userId)
    }

    override suspend fun getCurrentUser(): User? {
        return userApi.getCurrentUser().data
    }

    override suspend fun isAuthenticated(): Boolean {
        return authApi.getToken() != null
    }

    override suspend fun login(email: String, password: String): ResultState<Unit> {
        return try {
            val response = userApi.login(LoginRequest(email, password))
            apiClient.updateToken(response.token)
            ResultState.Success(Unit)
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Login failed")
        }
    }

    override suspend fun register(name: String, email: String, password: String): ResultState<Unit> {
        return try {
            val response = userApi.register(RegisterRequest(name, email, password))
            apiClient.updateToken(response.token)
            ResultState.Success(Unit)
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Registration failed")
        }
    }

    override suspend fun logout() {
        apiClient.clearToken()
    }
}
