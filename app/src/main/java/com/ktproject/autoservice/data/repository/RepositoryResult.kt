package com.ktproject.autoservice.data.repository

sealed class RepositoryResult<out T> {
    data class Success<out T>(val data: T) : RepositoryResult<T>()
    data class Error(val message: String) : RepositoryResult<Nothing>()
    data class NetworkError(val message: String = "Ошибка интернет-соединения") : RepositoryResult<Nothing>()
}