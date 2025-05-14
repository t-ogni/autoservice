package com.ktproject.autoservice.data.repository.fake

import com.ktproject.autoservice.data.model.Service
import com.ktproject.autoservice.data.remote.ApiClient
import com.ktproject.autoservice.data.repository.RepositoryResult
import com.ktproject.autoservice.data.repository.ServiceRepository
import kotlinx.coroutines.delay


class FakeServiceRepository(
    private val apiClient: ApiClient
) : ServiceRepository {

    private val services = mutableListOf(
        Service("1", "Замена масла", "Быстрая и качественная замена масла в двигателе", "1000 ₽"),
        Service("2", "Диагностика", "Полная компьютерная диагностика авто", "1000 ₽"),
        Service("3", "Шиномонтаж", "Сезонная замена шин и балансировка", "1000 ₽"),
        Service("4", "Замена тормозов", "Проверка и замена тормозных колодок и дисков", "30 000 ₽"),
        Service("5", "Мойка", "Быстрая наружная и внутренняя мойка", "Договорная"),
        Service("6", "Ремонт подвески", "Диагностика и ремонт подвески", "1000 ₽"),
        Service("7", "Покраска", "Локальная и полная покраска кузова", "1000 ₽"),
        Service("8", "Зарядка кондиционера", "Заправка и обслуживание кондиционера", "от 5000 ₽"),
        Service("9", "Зарядка кондиционера2", "Заправка и обслуживание кондиционера", "от 5000 ₽"),
        Service("10", "Зарядка кондиционера3", "Заправка и обслуживание кондиционера", "от 5000 ₽"),
        Service("11", "Зарядка кондиционерa4", "Заправка и обслуживание кондиционера", "от 5000 ₽")
    )

    private fun shouldFail(): Boolean = kotlin.random.Random.nextFloat() < 0.6

    override suspend fun getServiceById(serviceId: String): RepositoryResult<Service> {
        delay(1000)
        return if (shouldFail()) {
            RepositoryResult.Error("Ошибка получения услуги")
        } else {
            services.find { it.id == serviceId }?.let {
                RepositoryResult.Success(it)
            } ?: RepositoryResult.Error("Услуга не найдена")
        }
    }

    override suspend fun getAllServices(): RepositoryResult<List<Service>> {
        delay(500)
        return if (shouldFail()) {
            RepositoryResult.Error("Ошибка загрузки услуг")
        } else {
            RepositoryResult.Success(services)
        }
    }

    override suspend fun addService(name: String, description: String, price: String): RepositoryResult<Unit> {
        delay(300)
        return if (shouldFail()) {
            RepositoryResult.Error("Ошибка добавления услуги")
        } else try {
            services.add(
                Service(
                    id = kotlin.random.Random.nextInt(1000, 9999).toString(),
                    title = name,
                    description = description,
                    price = price
                )
            )
            RepositoryResult.Success(Unit)
        } catch (e: Exception) {
            RepositoryResult.Error("Ошибка: ${e.message}")
        }
    }

    override suspend fun deleteService(serviceId: String): RepositoryResult<Unit> {
        delay(300)
        return if (shouldFail()) {
            RepositoryResult.Error("Ошибка удаления услуги")
        } else {
            val removed = services.removeIf { it.id == serviceId }
            if (removed) {
                RepositoryResult.Success(Unit)
            } else {
                RepositoryResult.Error("Услуга не найдена")
            }
        }
    }
}
