
package com.ktproject.autoservice.ui.views.admin.requests

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ktproject.autoservice.data.model.Request
import com.ktproject.autoservice.ui.components.UIState
import com.ktproject.autoservice.ui.viewmodel.AdminRequestsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminRequestsScreen(
    onRequestClick: (String) -> Unit,
    viewModel: AdminRequestsViewModel = getViewModel()
) {
    val state by viewModel.requestsState.collectAsState()
    val servicesMap by viewModel.servicesMap.collectAsState()

    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.loadAllRequests()
    }

    fun handleRefresh() {
        isRefreshing = true
        coroutineScope.launch {
            viewModel.loadAllRequests()
            delay(300)
            isRefreshing = false
        }
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text("Все заявки") })
    }) { padding ->
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { handleRefresh() },
        modifier = Modifier.padding(padding)
    ) {
            Column(Modifier.padding(16.dp)) {
            when (state) {
                is UIState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Text("Загрузка заявок...", modifier = Modifier.padding(top = 8.dp))
                    }
                }
                is UIState.Error -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Text((state as UIState.Error).message)
                        }
                    }
                }
                is UIState.Success -> {
                    val requests = (state as UIState.Success<List<Request>>).data
                    if (requests.isEmpty()) {
                        Text("Нет активных заявок", modifier = Modifier.padding(16.dp))
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(requests) { request ->
                                RequestCard(
                                    request = request,
                                    serviceName = viewModel.getServiceNameById(request.serviceId),
                                    onClick = { onRequestClick(request.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
        }
    }
}

@Composable
fun RequestCard(
    request: Request,
    serviceName: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = serviceName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Автомобиль",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = if (request.carBrand.isNotEmpty() && request.carModel.isNotEmpty()) 
                        "${request.carBrand} ${request.carModel}" 
                    else "Автомобиль не указан",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Дата: ${request.date}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Время: ${request.time}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            StatusChip(status = request.status)
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val (backgroundColor, textColor, statusText) = when (status) {
        "wait" -> Triple(Color(0xFFE3F2FD), Color(0xFF1565C0), "Ожидание")
        "active" -> Triple(Color(0xFFF1F8E9), Color(0xFF33691E), "В работе")
        "completed" -> Triple(Color(0xFFE8F5E9), Color(0xFF2E7D32), "Выполнено")
        "canceled" -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), "Отменено")
        else -> Triple(Color(0xFFF5F5F5), Color.Gray, status)
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
    ) {
        Text(
            text = statusText,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}
