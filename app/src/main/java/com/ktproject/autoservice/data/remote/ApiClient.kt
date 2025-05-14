package com.ktproject.autoservice.data.remote

import com.ktproject.autoservice.api.responses.ApiResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass
import io.ktor.util.reflect.*

class ApiException(message: String) : Exception(message)

class ApiClient(
    val baseUrl: String,
    internal var token: String? = null
) {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    fun updateToken(newToken: String) {
        token = newToken
    }

    fun clearToken() {
        token = null
    }

    fun getToken(): String? {
        return token
    }


    suspend fun <T : Any> _get(path: String, responseTypeInfo: TypeInfo): T {
        val response = client.get("$baseUrl$path") {
            token?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        }
        return response.body(responseTypeInfo)
    }

    suspend fun <T : Any, B : Any> _post(path: String, body: B, responseTypeInfo: TypeInfo, bodyTypeInfo: TypeInfo): T {
        val response = client.post("$baseUrl$path") {
            contentType(ContentType.Application.Json)
            setBody(body, bodyTypeInfo)
            token?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        }
        return response.body(responseTypeInfo)
    }

    suspend fun <T : Any, B : Any> _put(path: String, body: B, responseTypeInfo: TypeInfo, bodyTypeInfo: TypeInfo): T {
        val response = client.put("$baseUrl$path") {
            contentType(ContentType.Application.Json)
            setBody(body, bodyTypeInfo)
            token?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        }
        return response.body(responseTypeInfo)
    }

    suspend fun <T : Any> _delete(path: String, responseTypeInfo: TypeInfo): T {
        val response = client.delete("$baseUrl$path") {
            token?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        }
        return response.body(responseTypeInfo)
    }
}

suspend inline fun <reified T : Any, reified B : Any> ApiClient.post(path: String, body: B): T {
    return _post(path, body, typeInfo<T>(), typeInfo<B>())
}

suspend inline fun <reified T : Any> ApiClient.get(path: String): T {
    return _get(path, typeInfo<T>())
}

suspend inline fun <reified T : Any, reified B : Any> ApiClient.put(path: String, body: B): T {
    return _put(path, body, typeInfo<T>(), typeInfo<B>())
}

suspend inline fun <reified T : Any> ApiClient.delete(path: String): T {
    return _delete(path, typeInfo<T>())
}
