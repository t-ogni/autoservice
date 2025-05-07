package com.ktproject.autoservice.data.remote

import com.ktproject.autoservice.data.model.AddUserRequest
import com.ktproject.autoservice.data.model.ApiResponse
import com.ktproject.autoservice.data.model.LoginRequest
import com.ktproject.autoservice.data.model.RegisterRequest
import com.ktproject.autoservice.data.model.TokenResponse
import com.ktproject.autoservice.data.model.UpdateUserRequest
import com.ktproject.autoservice.data.model.User
import com.ktproject.autoservice.data.repository.ResultState

class UserApi(private val apiClient: ApiClient) {

    suspend fun getAllUsers(): ApiResponse<List<User>> {
        return apiClient.safeGet("/users")
    }

    suspend fun getUserById(id: String): ApiResponse<User> {
        return apiClient.safeGet("/users/$id")
    }

    suspend fun addUser(request: AddUserRequest): ApiResponse<Unit> {
        return apiClient.safePost("/users", request)
    }

    suspend fun updateUser(id: String, request: UpdateUserRequest): ApiResponse<Unit> {
        return apiClient.safePut("/users/$id", request)
    }

    suspend fun deleteUser(id: String): ApiResponse<Unit> {
        return apiClient.safeDelete("/users/$id")
    }

    suspend fun getCurrentUser(): ApiResponse<User> {
        return apiClient.safeGet("/users/me")
    }
}

