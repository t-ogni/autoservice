package com.ktproject.autoservice.data.repository.fake

import android.content.res.Resources.NotFoundException
import com.ktproject.autoservice.data.model.Request
import com.ktproject.autoservice.data.model.User
import com.ktproject.autoservice.data.remote.ApiClient
import com.ktproject.autoservice.data.repository.RepositoryResult
import com.ktproject.autoservice.data.repository.RequestRepository
import com.ktproject.autoservice.data.repository.UserRepository
import kotlinx.coroutines.delay
import java.util.Date
class FakeRequestRepository(
    private val apiClient: ApiClient,
    private val userRepository: UserRepository
) : RequestRepository {

    private val requests = mutableListOf(
        Request("1", "101", "1", "Замена масла", "Toyota", "Camry", "active", "2025-04-30", "10:00", ""),
        Request("2", "102", "1", "Диагностика двигателя", "Honda", "Civic", "wait", "2025-04-30", "11:00", ""),
        Request("3", "103", "2", "Ремонт подвески", "BMW", "X5", "completed", "2025-04-30", "09:00", "Успешно выполнено"),
        Request("4", "104", "1", "Проверка тормозной системы", "Audi", "A4", "active", "2025-04-30", "09:00", ""),
        Request("5", "105", "3", "Установка сигнализации", "Mercedes", "E-Class", "wait", "2025-05-30", "09:00", ""),
        Request("6", "105", "3", "Установка сигнализации", "Lexus", "RX", "wait", "2025-05-03", "18:00", "")
    )

    private suspend fun getCurrentUserId(): String {
        when (val user = userRepository.getCurrentUser()) {
            is RepositoryResult.Success -> {
                return user.data.id
            }

            else -> {
                throw NotFoundException("User ID not found")
            }
        }
    }

    private fun shouldFail(): Boolean = kotlin.random.Random.nextFloat() < 0.1

    override suspend fun createRequest(serviceId: String, description: String, carBrand: String, carModel: String): RepositoryResult<String> {
        return createRequest(serviceId, description, "2025-05-01", "09:00", carBrand, carModel)
    }

    override suspend fun createRequest(serviceId: String, description: String, date: String, time: String, carBrand: String, carModel: String): RepositoryResult<String> {
        delay(500)
        return if (shouldFail()) {
            RepositoryResult.Error("Ошибка создания заявки")
        } else try {
            val userId = getCurrentUserId()
            val newId = (requests.maxBy { it.id.toIntOrNull() ?: 0 }.id.toInt() + 1).toString()
            val newRequest = Request(newId, serviceId, userId, description, carBrand, carModel, "wait", date, time, "")
            requests.add(newRequest)
            RepositoryResult.Success(newId)
        } catch (e: Exception) {
            RepositoryResult.Error("Ошибка: ${e.message}")
        }
    }

    override suspend fun getMyRequests(): RepositoryResult<List<Request>> {
        delay(300)
        return if (shouldFail()) {
            RepositoryResult.Error("Ошибка загрузки ваших заявок")
        } else try {
            val userId = getCurrentUserId()
            RepositoryResult.Success(requests.filter { it.userId == userId })
        } catch (e: Exception) {
            RepositoryResult.Error("Ошибка: ${e.message}")
        }
    }

    override suspend fun getAllRequests(): RepositoryResult<List<Request>> {
        delay(300)
        return if (shouldFail()) {
            RepositoryResult.Error("Ошибка загрузки всех заявок")
        } else {
            RepositoryResult.Success(requests)
        }
    }

    override suspend fun updateRequestStatus(requestId: String, status: String, result: String?): RepositoryResult<Unit> {
        delay(200)
        return if (shouldFail()) {
            RepositoryResult.Error("Ошибка обновления статуса")
        } else {
            val index = requests.indexOfFirst { it.id == requestId }
            if (index != -1) {
                val oldRequest = requests[index]
                requests[index] = oldRequest.copy(status = status, result = result ?: oldRequest.result)
                RepositoryResult.Success(Unit)
            } else {
                RepositoryResult.Error("Заявка не найдена")
            }
        }
    }
}
