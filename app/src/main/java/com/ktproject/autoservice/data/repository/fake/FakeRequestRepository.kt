package com.ktproject.autoservice.data.repository.fake

import com.ktproject.autoservice.data.model.Request
import com.ktproject.autoservice.data.remote.ApiClient
import com.ktproject.autoservice.data.repository.RequestRepository
import com.ktproject.autoservice.data.repository.UserRepository
import kotlinx.coroutines.delay
import java.util.Date

class FakeRequestRepository(
    private val apiClient: ApiClient,
    private val userRepository: UserRepository
) : RequestRepository {
    private val requests = mutableListOf<Request>(
        Request("1", "101", "1", "Замена масла", "active", "2025-04-30", "10:00"),
        Request("2", "102", "1", "Диагностика двигателя", "wait", "2025-04-30", "11:00"),
        Request("3", "103", "2", "Ремонт подвески", "completed", "2025-04-30", "09:00"),
        Request("4", "104", "1", "Проверка тормозной системы", "active", "2025-04-30", "09:00"),
        Request("5", "105", "3", "Установка сигнализации", "wait", "2025-05-30", "09:00"),
        Request("6", "105", "3", "Установка сигнализации", "wait", "2025-05-03", "18:00")
    )

    private suspend fun getCurrentUserId(): String {
        return userRepository.getCurrentUser()?.id.toString()
    }

    override suspend fun createRequest(serviceId: String, description: String): String {
        return createRequest(serviceId, description, "2025-05-01", "09:00")
    }

    override suspend fun createRequest(
        serviceId: String,
        description: String,
        date: String,
        time: String
    ): String {
        delay(500)
        val userId = getCurrentUserId()
        val newId = (requests.maxBy { it.id }.id.toInt() + 1).toString()
        val newRequest = Request(newId, serviceId, userId, description, "wait", date, time)
        requests.add(newRequest)
        return newId
    }

    override suspend fun getMyRequests(): List<Request> {
        delay(300)
        val userId = getCurrentUserId()
        return requests.filter { it.userId == userId }
    }

    override suspend fun getAllRequests(): List<Request> {
        delay(300)
        return requests
    }

    override suspend fun updateRequestStatus(requestId: String, status: String, result: String?) {
        delay(200)
        val index = requests.indexOfFirst { it.id == requestId }
        if (index != -1) {
            val oldRequest = requests[index]
            requests[index] = oldRequest.copy(status = status)
        }
    }
}
