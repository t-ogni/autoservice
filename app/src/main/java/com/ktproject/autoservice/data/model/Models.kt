package com.ktproject.autoservice.data.model

data class Service(
    val id: String,
    val title: String,
    val description: String,
    val price: String
)

data class User(
    val id: String,
    var name: String,
    var email: String,
    val role: String
)

data class Request(
    val id: String,
    val serviceId: String,
    val userId: String,
    val description: String,
    val status: String,
    val result: String = ""
)

data class News(
    val id: String,
    val title: String,
    val content: String,
    val date: String
)