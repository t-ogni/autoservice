package com.ktproject.autoservice.data.remote

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

class ApiClient(
    private val baseUrl: String,
    private var token: String? = null
) {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun <T : Any> get(path: String, typeInfo: TypeInfo): T {
        val requestToken = token
        val response = client.get("$baseUrl$path") {
            requestToken?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        }
        return response.body(typeInfo)
    }

    suspend fun <T : Any> post(path: String, body: Any? = null, typeInfo: TypeInfo): T {
        val requestToken = token
        val response = client.post("$baseUrl$path") {
            contentType(ContentType.Application.Json)
            requestToken?.let { header(HttpHeaders.Authorization, "Bearer $it") }
            body?.let { setBody(it) }
        }
        return response.body(typeInfo)
    }

    suspend fun <T : Any> put(path: String, body: Any? = null, typeInfo: TypeInfo): T {
        val requestToken = token
        val response = client.put("$baseUrl$path") {
            contentType(ContentType.Application.Json)
            requestToken?.let { header(HttpHeaders.Authorization, "Bearer $it") }
            body?.let { setBody(it) }
        }
        return response.body(typeInfo)
    }

    suspend fun <T : Any> delete(path: String, typeInfo: TypeInfo): T {
        val requestToken = token
        val response = client.delete("$baseUrl$path") {
            requestToken?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        }
        return response.body(typeInfo)
    }

    fun updateToken(newToken: String?) {
        token = newToken
    }

    fun clearToken() {
        token = null
    }

    fun getToken(): String? {
        return token
    }

}

suspend inline fun <reified T : Any> ApiClient.get(path: String): T {
    return this.get(path, typeInfo<T>())
}
suspend inline fun <reified T : Any> ApiClient.post(path: String, body: Any? = null): T {
    return this.post(path, body, typeInfo<T>())
}
suspend inline fun <reified T : Any> ApiClient.put(path: String, body: Any? = null): T {
    return this.put(path, body, typeInfo<T>())
}

suspend inline fun <reified T : Any> ApiClient.delete(path: String): T {
    return this.delete(path, typeInfo<T>())
}