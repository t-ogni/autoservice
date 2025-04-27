package com.ktproject.autoservice.data.repository.fake

import com.ktproject.autoservice.data.model.Service
import com.ktproject.autoservice.data.repository.ServiceRepository
import kotlinx.coroutines.delay
import kotlin.random.Random


class FakeServiceRepository : ServiceRepository {
    private val services = mutableListOf<Service>(
        Service("1", "Замена масла", "Быстрая и качественная замена масла в двигателе", "1000"),
        Service("2", "Диагностика", "Полная компьютерная диагностика авто", "1000"),
        Service("3", "Шиномонтаж", "Сезонная замена шин и балансировка", "1000"),
        Service("4", "Замена тормозов", "Проверка и замена тормозных колодок и дисков", "30 000"),
        Service("5", "Мойка", "Быстрая наружная и внутренняя мойка", "1000"),
        Service("6", "Ремонт подвески", "Диагностика и ремонт подвески", "1000"),
        Service("7", "Покраска", "Локальная и полная покраска кузова", "1000"),
        Service("8", "Зарядка кондиционера", "Заправка и обслуживание кондиционера", "1000"),
    )

    override suspend fun getServiceById(serviceId: String): Service? {
        delay(1000)
        return this.services.find { it.id == serviceId }
    }

    override suspend fun getAllServices(): List<Service> {
        delay(500) // Эмуляция задержки сети
        return services
    }

    override suspend fun addService(name: String, description: String, price: Int) {
        delay(300)
        services.add(
            Service(
                id = Random(System.currentTimeMillis()).nextInt(1000, 9999).toString(),
                title = name,
                description = description,
                price = price.toString()
            )
        )
    }

    override suspend fun deleteService(serviceId: String) {
        delay(300)
        services.removeIf { it.id == serviceId }
    }
}
