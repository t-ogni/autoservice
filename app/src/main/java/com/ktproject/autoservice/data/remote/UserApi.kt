package com.ktproject.autoservice.data.remote

import com.ktproject.autoservice.data.model.LoginRequest
import com.ktproject.autoservice.data.model.RegisterRequest
import com.ktproject.autoservice.data.model.TokenResponse
import com.ktproject.autoservice.data.model.UserResponse

class UserApi(private val apiClient: ApiClient) {

    suspend fun register(request: RegisterRequest): TokenResponse {
        return apiClient.post("/register", request)
    }

    suspend fun login(request: LoginRequest): TokenResponse {
        return apiClient.post("/login", request)
    }

    suspend fun getProfile(): UserResponse {
        return apiClient.get("/me")
    }
}
