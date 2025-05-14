package com.ktproject.autoservice.api.requests


import kotlinx.serialization.Serializable


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
data class AddUserRequest(
    val name: String,
    val email: String,
    val role: String
)

@Serializable
data class UpdateUserRequest(
    val name: String? = null,
    val email: String? = null,
    val role: String? = null
)

@Serializable
data class AddServiceRequest(
    val title: String,
    val description: String,
    val price: String
)

@Serializable
data class CreateRequestRequest(
    val serviceId: String,
    val description: String,
    val date: String,  // yyyy-MM-dd
    val time: String   // HH:mm
)

@Serializable
data class UpdateRequestStatusRequest(
    val status: String,
    val result: String? = null
)

@Serializable
data class AddNewsRequest(
    val title: String,
    val content: String,
    val date: String
)

@Serializable
data class UpdateNewsRequest(
    val title: String,
    val content: String,
    val date: String
)
