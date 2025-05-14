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
    var role: String
)
data class Request(
    val id: String,
    val serviceId: String,
    val userId: String,
    val description: String,
    val carBrand: String = "",
    val carModel: String = "",
    val status: String,
    val date: String,       // yyyy-MM-dd
    val time: String,       // HH:mm
    val result: String = ""
)

data class News(
    val id: String,
    val title: String,
    val content: String,
    val date: String
)