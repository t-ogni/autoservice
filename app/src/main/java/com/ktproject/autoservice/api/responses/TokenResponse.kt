package com.ktproject.autoservice.api.responses

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseData(
    val token: String,
    val role: String
)

@Serializable
data class UserResponseData(
    val id: Int,
    val name: String,
    val role: String
)
