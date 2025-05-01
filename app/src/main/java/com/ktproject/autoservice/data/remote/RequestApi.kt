package com.ktproject.autoservice.data.remote

import com.ktproject.autoservice.data.model.GetRequest
import com.ktproject.autoservice.data.model.Request

class RequestApi(private val apiClient: ApiClient) {

    suspend fun getUserRequests(): List<GetRequest> {
        return apiClient.get("/requests")
    }

    suspend fun createRequest(request: Request): GetRequest {
        return apiClient.post("/requests", request)
    }

    suspend fun getRequestById(id: Int): GetRequest {
        return apiClient.get("/requests/$id")
    }

    suspend fun updateRequest(id: Int, request: Request): GetRequest {
        return apiClient.put("/requests/$id", request)
    }

    suspend fun deleteRequest(id: Int) {
        apiClient.delete<Unit>("/requests/$id")
    }
}
