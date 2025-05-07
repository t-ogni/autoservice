package com.ktproject.autoservice.data.remote

import com.ktproject.autoservice.data.model.ApiResponse
import com.ktproject.autoservice.data.model.LoginRequest
import com.ktproject.autoservice.data.model.RegisterRequest

class AuthApi(private val apiClient: ApiClient) {

    suspend fun register(request: RegisterRequest): ApiResponse<String> {
        return apiClient.safePost("/auth/register", request)
    }

    suspend fun login(request: LoginRequest): ApiResponse<String> {
        return apiClient.safePost("/auth/login", request)
    }

    suspend fun logout(): ApiResponse<Unit> {
        return apiClient.safePost("/auth/logout")
    }

}
