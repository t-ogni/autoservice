package com.ktproject.autoservice.ui.views.admin.requests

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ktproject.autoservice.data.model.Request
import com.ktproject.autoservice.ui.components.UIState
import com.ktproject.autoservice.ui.viewmodel.AdminRequestsViewModel
import org.koin.androidx.compose.getViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminRequestDetailScreen(
    requestId: String,
    viewModel: AdminRequestsViewModel = getViewModel()
) {
    val request by viewModel.selectedRequest.collectAsState()
    var status by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadRequestById(requestId)
    }

    LaunchedEffect(request) {
        request?.let {
            status = it.status
            result = it.result ?: ""
        }
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text("Редактировать заявку") })
    }) { padding ->
        request?.let {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("ID: ${it.id}", style = MaterialTheme.typography.titleMedium)
                Text("Услуга: ${it.serviceId}")

                OutlinedTextField(
                    value = status,
                    onValueChange = { status = it },
                    label = { Text("Статус") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = result,
                    onValueChange = { result = it },
                    label = { Text("Результат") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(onClick = {
                    viewModel.updateRequestStatus(it.id, status, result)
                }) {
                    Text("Сохранить")
                }
            }
        } ?: Text("Загрузка...", modifier = Modifier.padding(16.dp))
    }
}
