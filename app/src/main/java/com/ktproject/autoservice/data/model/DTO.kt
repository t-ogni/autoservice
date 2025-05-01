package com.ktproject.autoservice.data.model


import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: String? = null
)

@Serializable
data class GetNews(
    val id: Int,
    val title: String,
    val content: String,
    val date: String
)

@Serializable
data class GetRequest(
    val id: Int,
    val userId: Int,
    val serviceId: Int,
    val date: String,
    val carBrand: String,
    val customerComment: String,
    val status: String,
    val result: String? = null
)

@Serializable
data class GetService(
    val id: Int,
    val name: String,
    val price: Double,
    val description: String
)

@Serializable
data class GetUser(
    val id: Int,
    val name: String,
    val email: String,
    val passwordHash: String,
    val phone: String,
    val role: String
)

@Serializable
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
    val role: String
)

@Serializable
data class TokenResponse(
    val token: String
)
