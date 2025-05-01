package com.ktproject.autoservice.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.repository.*
import com.ktproject.autoservice.data.model.*
import com.ktproject.autoservice.data.repository.in_memory.NewRequestDraftRepository
import com.ktproject.autoservice.ui.components.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class NewRequestData(
    val carModel: String = "",
    val comment: String = "",
    val selectedServiceId: String = "",
    val selectedDate: String? = null,
    val selectedTime: String? = null
)

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
            try {
                val loaded = serviceRepository.getAllServices()
                preselectedServiceId?.let {
                    updateData { it.copy(selectedServiceId = preselectedServiceId) }
                }
                _servicesState.value = UIState.Success(loaded)
            } catch (e: Exception) {
                _servicesState.value = UIState.Error("Ошибка загрузки: ${e.message}")
            }
        }
    }

    fun loadBusyDates() {
        viewModelScope.launch {
            try {
                val allRequests = requestRepository.getAllRequests()
                val busy = allRequests.groupBy { it.date }.filterValues { it.size >= 4 }.keys.toList()
                _busyDates.value = busy
            } catch (e: Exception) {
                _busyDates.value = emptyList()
            }
        }
    }

    private fun updateData(update: (NewRequestData) -> NewRequestData) {
        val updated = update(_requestData.value)
        _requestData.value = updated
        draftRepository.updateDraft(updated)
        Log.d("NEW REQUEST", "createRequest $updated")
    }

    fun updateCarInfo(carModel: String, comment: String) =
        updateData { it.copy(carModel = carModel, comment = comment) }

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
            val requestId = requestRepository.createRequest(
                serviceId = current.selectedServiceId,
                description = current.comment,
                date = current.selectedDate ?: "0001-01-01",
                time = current.selectedTime ?: "00:00"
            )
            Log.d("NEW REQUEST", "createRequest ID $requestId")
        }
    }
}
