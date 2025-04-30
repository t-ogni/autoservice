package com.ktproject.autoservice.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.data.repository.RequestRepository
import com.ktproject.autoservice.data.repository.ServiceRepository
import com.ktproject.autoservice.data.model.Service
import com.ktproject.autoservice.data.model.Request
import com.ktproject.autoservice.data.repository.UserRepository
import com.ktproject.autoservice.ui.components.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class NewRequestData(
    val carBrand: String = "",
    val carModel: String = "",
    val comment: String = "",
    val selectedServiceId: String = "",
    val serviceDescription: String = "",
    val selectedDate: String? = null
)

class NewRequestViewModel(
    private val requestRepository: RequestRepository,
    private val userRepository: UserRepository,
    private val serviceRepository: ServiceRepository
) : ViewModel() {

    private val _requestData = MutableStateFlow(NewRequestData())
    val requestData: StateFlow<NewRequestData> = _requestData

    private val _requestStatus = MutableStateFlow<Request?>(null)
    val requestStatus: StateFlow<Request?> = _requestStatus

    private val _uiState = MutableStateFlow<UIState<List<Service>>>(UIState.Loading)
    val uiState: StateFlow<UIState<List<Service>>> = _uiState

    fun loadServices(preselectedServiceId: String? = null) {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            try {
                val loaded = serviceRepository.getAllServices()
                preselectedServiceId?.let {
                    _requestData.value = _requestData.value.copy(selectedServiceId = it)
                }
                _uiState.value = UIState.Success(loaded)
            } catch (e: Exception) {
                _uiState.value = UIState.Error("Ошибка загрузки: ${e.message}")
            }
        }
    }


    private val _busyDates = MutableStateFlow<List<String>>(emptyList())
    val busyDates: StateFlow<List<String>> = _busyDates

    fun loadBusyDates() {
        _busyDates.value = listOf(
            "2025-05-01",
            "2025-05-03",
            "2025-05-04"
        )
    }

    fun updateCarInfo(carBrand: String, carModel: String, comment: String) {
        _requestData.value = _requestData.value.copy(
            carBrand = carBrand,
            carModel = carModel,
            comment = comment
        )
    }

    fun updateSelectedService(serviceId: String) {
        _requestData.value = _requestData.value.copy(selectedServiceId = serviceId)
    }

    fun updateServiceDescription(description: String) {
        _requestData.value = _requestData.value.copy(serviceDescription = description)
    }

    fun updateSelectedDate(date: String) {
        _requestData.value = _requestData.value.copy(selectedDate = date)
    }

    fun createRequest() {
        viewModelScope.launch {
            val requestId = requestRepository.createRequest(
                serviceId = _requestData.value.selectedServiceId ?: "0",
                description = _requestData.value.serviceDescription
            )

            _requestStatus.value = Request(
                id = requestId,
                serviceId = (_requestData.value.selectedServiceId ?: 0).toString(),
                description = _requestData.value.serviceDescription,
                userId = userRepository.getCurrentUserId()?.id ?: "1",
                status = "open"
            )
        }
    }
}
