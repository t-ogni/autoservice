package com.ktproject.autoservice.ui.views.request

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ktproject.autoservice.data.model.Request
import com.ktproject.autoservice.ui.components.UIState
import com.ktproject.autoservice.ui.viewmodel.MyRequestsViewModel
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRequestsScreen(
    viewModel : MyRequestsViewModel = getViewModel(),
    onNewRequestClick: () -> Unit,
    onRequestClick: (String) -> Unit
) {
    val state by viewModel.requestsState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Мои заявки") },
                actions = {
                IconButton(onClick = onNewRequestClick) {
                    Text("+", style = MaterialTheme.typography.headlineMedium)
                }
            })
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (state) {
                is UIState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is UIState.Error -> {
                    val message = (state as UIState.Error).message
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = message, color = MaterialTheme.colorScheme.error)
                    }
                }

                is UIState.Success -> {
                    val requests = (state as UIState.Success<List<*>>).data.filterIsInstance<com.ktproject.autoservice.data.model.Request>()
                    if (requests.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("У вас пока нет заявок.")
                        }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                            items(requests) { request ->
                                RequestCard(request = request, onClick=onRequestClick)
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RequestCard(request: Request, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .clickable(onClick = { onClick(request.id) }),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("ID: ${request.id}", style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.height(4.dp))
            
            // Отображаем сервис, нужно будет добавить получение имени сервиса
            Text("Дата: ${request.date}, ${request.time}", 
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            
            Text("Статус: ${
                when(request.status) {
                    "wait" -> "Ожидание"
                    "active" -> "В работе"
                    "completed" -> "Выполнено"
                    "canceled" -> "Отменено"
                    else -> request.status
                }
            }", 
                color = when(request.status) {
                    "wait" -> MaterialTheme.colorScheme.tertiary
                    "active" -> MaterialTheme.colorScheme.primary
                    "completed" -> MaterialTheme.colorScheme.secondary
                    "canceled" -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.primary
                })
                
            if (request.result.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text("Результат: ${request.result}")
            }
        }
    }
}
