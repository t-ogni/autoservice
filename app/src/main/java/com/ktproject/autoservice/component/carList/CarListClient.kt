package com.ktproject.autoservice.component.carList// data/remote/KtorClientProvider.kt
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.gson.*

val carListClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        gson()
    }
}