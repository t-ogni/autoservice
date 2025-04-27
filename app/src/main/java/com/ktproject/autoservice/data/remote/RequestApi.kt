package com.ktproject.autoservice.data.remote

import com.ktproject.autoservice.data.model.ExposedRequest

class RequestApi(private val apiClient: ApiClient) {

    suspend fun getUserRequests(): List<ExposedRequest> {
        return apiClient.get("/requests")
    }

    suspend fun createRequest(request: ExposedRequest): ExposedRequest {
        return apiClient.post("/requests", request)
    }

    suspend fun getRequestById(id: Int): ExposedRequest {
        return apiClient.get("/requests/$id")
    }

    suspend fun updateRequest(id: Int, request: ExposedRequest): ExposedRequest {
        return apiClient.put("/requests/$id", request)
    }

    suspend fun deleteRequest(id: Int) {
        apiClient.delete<Unit>("/requests/$id")
    }
}
