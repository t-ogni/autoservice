package com.ktproject.autoservice.data.repository.fake

import com.ktproject.autoservice.data.model.Request
import com.ktproject.autoservice.data.model.Service
import com.ktproject.autoservice.data.remote.ApiClient
import com.ktproject.autoservice.data.repository.RequestRepository
import com.ktproject.autoservice.data.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeRequestRepository(
    private val apiClient: ApiClient,
    private val userRepository: UserRepository // <-- Вот тут внедрим UserRepository
) : RequestRepository {

    private val requests = mutableListOf<Request>(
//        Request("1", "101", "1", "Замена масла", "active"),
//        Request("2", "102", "1", "Диагностика двигателя", "wait"),
        Request("3", "103", "2", "Ремонт подвески", "completed"),
//        Request("4", "104", "1", "Проверка тормозной системы", "active"),
        Request("5", "105", "3", "Установка сигнализации", "wait")
    )

    private suspend fun getCurrentUserId(): String {
        // Можем тут эмулировать текущего пользователя — например всегда id "1"
        return userRepository.getCurrentUserId()?.id.toString()
    }

    override suspend fun createRequest(serviceId: String, description: String): String {
        delay(500)
        val userId = getCurrentUserId()
        val newId = (requests.size + 1).toString()
        val newRequest = Request(newId, serviceId.toString(), userId, description, "wait")
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
