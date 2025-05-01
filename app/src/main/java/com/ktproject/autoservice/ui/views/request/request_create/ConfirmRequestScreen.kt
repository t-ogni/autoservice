package com.ktproject.autoservice.ui.views.request.request_create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ktproject.autoservice.data.model.Service
import com.ktproject.autoservice.ui.components.UIState
import com.ktproject.autoservice.ui.viewmodel.NewRequestViewModel
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmRequestScreen(viewModel: NewRequestViewModel = getViewModel(), onSubmit: () -> Unit) {
    val requestStatus by viewModel.requestStatus.collectAsState()
    val requestData by viewModel.requestData.collectAsState()
    val servicesState by viewModel.servicesState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadServices()
    }

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("Подтверждение заявки") }) }) {
        Column(modifier = Modifier.padding(it.calculateTopPadding() + 16.dp)) {

            when (servicesState) {
                is UIState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is UIState.Error -> {
                    Text(
                        text = "Ошибка загрузки",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is UIState.Success -> {
                    val services = (servicesState as UIState.Success<List<Service>>).data
                    val selectedServiceId = requestData.selectedServiceId
                    val serviceName: String = services.find { it.id == selectedServiceId}?.title ?: "Услуга не найдена"
                    Text("Услуга: $serviceName")
                }
            }

            Text("описание: ${requestData.comment}")
            Text("Автомобиль: ${requestData.carModel}")
            Text("Время записи: ${requestData.selectedDate} ${requestData.selectedTime}")

            requestStatus?.let { Text("Ваша заявка на сервис: ${it.serviceId}") }

            Button(
                onClick = {
                    viewModel.createRequest()
                    onSubmit()
                }
            ) {
                Text("Записаться")
            }
        }
    }
}
