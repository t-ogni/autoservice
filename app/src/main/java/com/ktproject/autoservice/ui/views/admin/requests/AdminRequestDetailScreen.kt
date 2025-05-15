
package com.ktproject.autoservice.ui.views.admin.requests

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ktproject.autoservice.data.model.Request
import com.ktproject.autoservice.ui.components.UIState
import com.ktproject.autoservice.ui.viewmodel.AdminRequestsViewModel
import org.koin.androidx.compose.getViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminRequestDetailScreen(
    requestId: String,
    onNavigateBack: () -> Unit = {},
    viewModel: AdminRequestsViewModel = getViewModel()
) {
    val request by viewModel.selectedRequest.collectAsState()
    val updateState by viewModel.updateOperationState.collectAsState()
    var status by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.loadRequestById(requestId)
    }

    LaunchedEffect(request) {
        request?.let {
            status = it.status
            result = it.result
            description = it.description
        }
    }

    // Обработка состояния обновления
    LaunchedEffect(updateState) {
        if (updateState is UIState.Success) {
            // Если обновление успешно, вернуться на предыдущий экран
            onNavigateBack()
            viewModel.resetUpdateState()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Заявка №$requestId") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Вернуться назад"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when {
                request == null && (updateState == null || updateState !is UIState.Loading) -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Text("Загрузка заявки...", modifier = Modifier.padding(top = 8.dp))
                    }
                }
                updateState is UIState.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Text("Обновление статуса...", modifier = Modifier.padding(top = 8.dp))
                    }
                }
                updateState is UIState.Error -> {
                    Snackbar(
                        modifier = Modifier.padding(16.dp),
                        action = {
                            Button(onClick = { viewModel.resetUpdateState() }) {
                                Text("ОК")
                            }
                        }
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            item {
                                Text((updateState as UIState.Error).message)
                            }
                        }
                    }
                }
                request != null -> {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        request?.let { currentRequest ->
                            // Информация о сервисе
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        "Информация о заявке",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    
                                    Text(
                                        "Услуга: ${viewModel.getServiceNameById(currentRequest.serviceId)}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    
                                    Text(
                                        "Дата: ${currentRequest.date}, Время: ${currentRequest.time}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                            
                            // Информация об автомобиле
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.ShoppingCart,
                                            contentDescription = "Автомобиль",
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                        Text(
                                            "Информация об автомобиле",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    
                                    if (currentRequest.carBrand.isNotEmpty() || currentRequest.carModel.isNotEmpty()) {
                                        Text(
                                            "Марка: ${currentRequest.carBrand.ifEmpty { "Не указана" }}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            "Модель: ${currentRequest.carModel.ifEmpty { "Не указана" }}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    } else {
                                        Text(
                                            "Информация об автомобиле не предоставлена",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                            
                            // Комментарий клиента
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.Info,
                                            contentDescription = "Комментарий",
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                        Text(
                                            "Комментарий клиента",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    
                                    OutlinedTextField(
                                        value = description,
                                        onValueChange = { description = it },
                                        label = { Text("Комментарий") },
                                        modifier = Modifier.fillMaxWidth(),
                                        readOnly = true // Сделать только для чтения
                                    )
                                }
                            }
                            
                            // Управление статусом
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        "Управление заявкой",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    
                                    Box {
                                        OutlinedTextField(
                                            value = when(status) {
                                                "wait" -> "Ожидание"
                                                "active" -> "В работе"
                                                "completed" -> "Выполнено"
                                                "canceled" -> "Отменено"
                                                else -> status
                                            },
                                            onValueChange = {},
                                            label = { Text("Статус") },
                                            modifier = Modifier.fillMaxWidth(),
                                            readOnly = true,
                                            trailingIcon = {
                                                IconButton(onClick = { expanded = true }) {
                                                    Icon(
                                                        imageVector = Icons.Default.DateRange,
                                                        contentDescription = "Выбрать статус",
                                                        modifier = Modifier.padding(end = 8.dp)
                                                    )
                                                }
                                            }
                                        )
                                        
                                        DropdownMenu(
                                            expanded = expanded,
                                            onDismissRequest = { expanded = false }
                                        ) {
                                            viewModel.availableStatuses.forEach { statusOption ->
                                                val displayText = when(statusOption) {
                                                    "wait" -> "Ожидание"
                                                    "active" -> "В работе"
                                                    "completed" -> "Выполнено"
                                                    "canceled" -> "Отменено"
                                                    else -> statusOption
                                                }
                                                
                                                DropdownMenuItem(
                                                    text = { Text(displayText) },
                                                    onClick = {
                                                        status = statusOption
                                                        expanded = false
                                                    }
                                                )
                                            }
                                        }
                                    }
                                    
                                    OutlinedTextField(
                                        value = result,
                                        onValueChange = { result = it },
                                        label = { Text("Результат выполнения") },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    
                                    Spacer(modifier = Modifier.height(8.dp))
                                    
                                    Button(
                                        onClick = {
                                            viewModel.updateRequestStatus(
                                                currentRequest.id,
                                                status,
                                                result
                                            )
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Сохранить изменения")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
