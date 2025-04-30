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
import com.ktproject.autoservice.ui.viewmodel.UserRequestsViewModel
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestDetailsScreen(
    requestId: String,
    viewModel: UserRequestsViewModel = getViewModel()
) {
    val state by viewModel.requestsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMyRequests()
    }

    val request = (state as? UIState.Success)?.data?.find { it.id == requestId }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text("Заявка #$requestId") })
    }) { padding ->
        request?.let {
            Column(Modifier.padding(padding).padding(16.dp)) {
                Text("Услуга: ${it.serviceId}", style = MaterialTheme.typography.titleMedium)
                Text("Описание: ${it.description}")
                Text("Статус: ${it.status}")
                if (it.result.isNotBlank()) {
                    Text("Результат: ${it.result}")
                }
            }
        } ?: run {
            Text("Заявка не найдена", modifier = Modifier.padding(16.dp))
        }
    }
}
