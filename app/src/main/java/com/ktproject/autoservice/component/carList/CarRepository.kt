package com.ktproject.autoservice.component.carList

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class CarRepository(private val client: HttpClient) {

    private var selectedMake: CarMake? = null

    suspend fun searchMakes(query: String): List<CarMake> {
        Log.d("CarRepository", "Поиск марок по запросу: $query")
        return try {
            val makesResponse: CarMakeResponse = client.get("https://vpic.nhtsa.dot.gov/api/vehicles/getallmakes") {
                parameter("format", "json")
            }.body()

            val filteredMakes = makesResponse.Results.filter {
                it.Make_Name.contains(query, ignoreCase = true)
            }.take(5)

            Log.d("CarRepository", "Найдено марок: ${filteredMakes.size}")
            filteredMakes
        } catch (e: Exception) {
            Log.e("CarRepository", "Ошибка при получении марок: ${e.message}", e)
            emptyList()
        }
    }

    fun setMakeByName(name: String) {
        selectedMake = CarMake(0, name) // ID неизвестен, ставим 0
        Log.d("CarRepository", "Установлена марка по имени: $name")
    }

    fun setMakeById(id: Int, name: String) {
        selectedMake = CarMake(id, name)
        Log.d("CarRepository", "Установлена марка по ID: $id, Name: $name")
    }

    suspend fun searchCars(query: String): List<String> {
        if (selectedMake == null) {
            Log.w("CarRepository", "Марка не выбрана, возвращаю заглушку")
            return listOf("Не выбрана марка")
        }

        val makeName = selectedMake!!.Make_Name
        Log.d("CarRepository", "Поиск моделей для марки: $makeName, по запросу: $query")

        return try {
            val modelsResponse: CarModelResponse = client.get("https://vpic.nhtsa.dot.gov/api/vehicles/getmodelsformake/$makeName") {
                parameter("format", "json")
            }.body()

            val filteredModels = modelsResponse.Results.filter {
                it.Model_Name.contains(query, ignoreCase = true)
            }

            val result = filteredModels.map { "${it.Make_Name} ${it.Model_Name}" }
                .distinct()
                .take(10)

            Log.d("CarRepository", "Найдено моделей: ${result.size}")
            result
        } catch (e: Exception) {
            Log.e("CarRepository", "Ошибка при получении моделей: ${e.message}", e)
            emptyList()
        }
    }
}
