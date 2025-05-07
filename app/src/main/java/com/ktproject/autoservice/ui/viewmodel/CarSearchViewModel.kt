package com.ktproject.autoservice.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktproject.autoservice.component.carList.CarRepository
import com.ktproject.autoservice.component.carList.CarMake
import com.ktproject.autoservice.component.carList.SelectedMakeStore
import kotlinx.coroutines.launch

class CarSearchViewModel(
    private val repository: CarRepository,
    private val makeStore: SelectedMakeStore
) : ViewModel() {

    var query by mutableStateOf("")
    var suggestions by mutableStateOf(emptyList<String>())
        private set

    var makeQuery by mutableStateOf("")
    var makeSuggestions by mutableStateOf(emptyList<CarMake>())
        private set

    var selectedMake by mutableStateOf<String?>(null)
        private set

    init {
        viewModelScope.launch {
            val savedMake = makeStore.loadMake()
            savedMake?.let {
                repository.setMakeByName(it)
                selectedMake = it
            }
        }
    }

    fun onMakeQueryChange(input: String) {
        makeQuery = input
        viewModelScope.launch {
            makeSuggestions = if (input.length > 1) repository.searchMakes(input) else emptyList()
        }
    }

    fun selectMake(make: CarMake) {
        repository.setMakeById(make.Make_ID, make.Make_Name)
        selectedMake = make.Make_Name
        makeQuery = make.Make_Name
        makeSuggestions = emptyList()

        viewModelScope.launch {
            makeStore.saveMake(make.Make_Name)
        }
    }

    fun onCarQueryChange(carQuery: String) {
        query = carQuery
        viewModelScope.launch {
            suggestions = if (carQuery.length > 2) repository.searchCars(carQuery) else emptyList()
        }
    }

    fun selectSuggestion(value: String) {
        query = value
        suggestions = emptyList()
    }
}
