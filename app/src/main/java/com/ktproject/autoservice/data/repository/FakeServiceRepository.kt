package com.ktproject.autoservice.data.repository


import com.ktproject.autoservice.data.model.Service

object FakeServiceRepository {
    private val services = listOf(
        Service("1", "Замена масла", "Быстрая и качественная замена масла в двигателе", "https://example.com/icons/oil.png"),
        Service("2", "Диагностика", "Полная компьютерная диагностика авто", "https://example.com/icons/diagnostic.png"),
        Service("3", "Шиномонтаж", "Сезонная замена шин и балансировка", "https://example.com/icons/tires.png"),
        Service("4", "Замена тормозов", "Проверка и замена тормозных колодок и дисков", "https://example.com/icons/brakes.png"),
        Service("5", "Мойка", "Быстрая наружная и внутренняя мойка", "https://example.com/icons/wash.png"),
        Service("6", "Ремонт подвески", "Диагностика и ремонт подвески", "https://example.com/icons/suspension.png"),
        Service("7", "Покраска", "Локальная и полная покраска кузова", "https://example.com/icons/paint.png"),
        Service("8", "Зарядка кондиционера", "Заправка и обслуживание кондиционера", "https://example.com/icons/ac.png"),
        Service("9", "Ремонт двигателя", "Капитальный ремонт и замена деталей двигателя", "https://example.com/icons/engine.png"),
        Service("10", "Тюнинг", "Индивидуальный тюнинг салона и внешнего вида", "https://example.com/icons/tuning.png")
    )

    fun getAllServices(): List<Service> = services

    fun getServiceById(id: String): Service? = services.find { it.id == id }
}
