package com.ktproject.autoservice.data.remote

import com.ktproject.autoservice.data.model.AddServiceRequest
import com.ktproject.autoservice.data.model.ApiResponse
import com.ktproject.autoservice.data.model.Service

class ServiceApi(private val apiClient: ApiClient) {

    suspend fun getAllServices(): ApiResponse<List<Service>> {
        return apiClient.safeGet("/services")
    }

    suspend fun getServiceById(id: String): ApiResponse<Service> {
        return apiClient.safeGet("/services/$id")
    }

    suspend fun addService(request: AddServiceRequest): ApiResponse<Unit> {
        return apiClient.safePost("/services", request)
    }

    suspend fun deleteService(id: String): ApiResponse<Unit> {
        return apiClient.safeDelete("/services/$id")
    }
}
