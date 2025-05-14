package com.ktproject.autoservice.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.repository.*
import com.ktproject.autoservice.data.model.*
import com.ktproject.autoservice.data.repository.runtime.NewRequestDraftRepository
import com.ktproject.autoservice.ui.components.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class NewRequestData(
    val carModel: String = "",
    val carBrand: String = "",
    val comment: String = "",
    val selectedServiceId: String = "",
    val selectedDate: String? = null,
    val selectedTime: String? = null
) {
}

class NewRequestViewModel(
    private val requestRepository: RequestRepository,
    private val userRepository: UserRepository,
    private val serviceRepository: ServiceRepository,
    private val draftRepository: NewRequestDraftRepository
) : ViewModel() {

    private val _requestData = MutableStateFlow(draftRepository.getDraft())
    val requestData: StateFlow<NewRequestData> = _requestData

    private val _requestStatus = MutableStateFlow<Request?>(null)
    val requestStatus: StateFlow<Request?> = _requestStatus

    private val _servicesState = MutableStateFlow<UIState<List<Service>>>(UIState.Loading)
    val servicesState: StateFlow<UIState<List<Service>>> = _servicesState

    private val _busyDates = MutableStateFlow<List<String>>(emptyList())
    val busyDates: StateFlow<List<String>> = _busyDates

    fun loadServices(preselectedServiceId: String? = null) {
        viewModelScope.launch {
            _servicesState.value = UIState.Loading
            when (val result = serviceRepository.getAllServices()) {
                is RepositoryResult.Success -> {
                    preselectedServiceId?.let {
                        updateData { it.copy(selectedServiceId = preselectedServiceId) }
                    }
                    _servicesState.value = UIState.Success(result.data)
                }
                is RepositoryResult.Error -> {
                    _servicesState.value = UIState.Error("Ошибка загрузки: ${result.message}")
                }
                is RepositoryResult.NetworkError -> {
                    _servicesState.value = UIState.Error("Ошибка соединения: ${result.message}")
                }
            }
        }
    }

    fun loadBusyDates() {
        viewModelScope.launch {
            when (val result = requestRepository.getAllRequests()) {
                is RepositoryResult.Success -> {
                    val busy = result.data.groupBy { it.date }.filterValues { it.size >= 4 }.keys.toList()
                    _busyDates.value = busy
                }
                is RepositoryResult.Error, is RepositoryResult.NetworkError -> {
                    _busyDates.value = emptyList()
                }
            }
        }
    }

    private fun updateData(update: (NewRequestData) -> NewRequestData) {
        val updated = update(_requestData.value)
        _requestData.value = updated
        draftRepository.updateDraft(updated)
        Log.d("NEW REQUEST", "createRequest $updated")
    }

    fun updateCarInfo(carModel: String, comment: String, carBrand: String) =
        updateData { it.copy(carModel = carModel, comment = comment, carBrand = carBrand) }

    fun updateSelectedService(serviceId: String) =
        updateData { it.copy(selectedServiceId = serviceId) }

    fun updateSelectedDate(date: String) =
        updateData { it.copy(selectedDate = date) }

    fun updateSelectedTime(time: String) =
        updateData { it.copy(selectedTime = time) }

    fun clearDraft() {
        draftRepository.clearDraft()
        _requestData.value = draftRepository.getDraft()
    }

    fun createRequest() {
        viewModelScope.launch {
            val current = _requestData.value
            when (val result = requestRepository.createRequest(
                serviceId = current.selectedServiceId,
                description = current.comment,
                date = current.selectedDate ?: "0001-01-01",
                time = current.selectedTime ?: "00:00",
                carBrand = current.carBrand, // Добавляем марку, если есть
                carModel = current.carModel // Добавляем модель автомобиля
            )) {
                is RepositoryResult.Success -> {
                    Log.d("NEW REQUEST", "createRequest ID ${result.data}")
                    clearDraft() // Очищаем черновик после успешного создания
                }
                is RepositoryResult.Error -> {
                    Log.e("NEW REQUEST", "Error creating request: ${result.message}")
                }
                is RepositoryResult.NetworkError -> {
                    Log.e("NEW REQUEST", "Network error: ${result.message}")
                }
            }
        }
    }
}
