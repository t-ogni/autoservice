package com.ktproject.autoservice.data.remote

import com.ktproject.autoservice.data.model.ApiResponse
import com.ktproject.autoservice.data.model.CreateRequestRequest
import com.ktproject.autoservice.data.model.Request
import com.ktproject.autoservice.data.model.UpdateRequestStatusRequest

class RequestApi(private val apiClient: ApiClient) {

    suspend fun createRequest(request: CreateRequestRequest): ApiResponse<String> {
        return apiClient.safePost("/requests", request)
    }

    suspend fun getMyRequests(): ApiResponse<List<Request>> {
        return apiClient.safeGet("/requests/mine")
    }

    suspend fun getAllRequests(): ApiResponse<List<Request>> {
        return apiClient.safeGet("/requests")
    }

    suspend fun updateRequestStatus(id: String, request: UpdateRequestStatusRequest): ApiResponse<Unit> {
        return apiClient.safePut("/requests/$id/status", request)
    }

    suspend fun getRequestById(id: String): ApiResponse<Request> {
        return apiClient.safeGet("/requests/$id")
    }

    suspend fun deleteRequest(id: String): ApiResponse<Unit> {
        return apiClient.safeDelete("/requests/$id")
    }
}
