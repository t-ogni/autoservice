package com.ktproject.autoservice.ui.views.admin.service

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.ktproject.autoservice.data.model.Service
import com.ktproject.autoservice.ui.components.UIState
import com.ktproject.autoservice.ui.viewmodel.AdminRequestsViewModel
import com.ktproject.autoservice.ui.viewmodel.AdminServiceViewModel
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminServiceDetailScreen(
    serviceId: String,
    onSave : () -> Unit,
    viewModel: AdminServiceViewModel = getViewModel()
) {
    val service by viewModel.selectedService.collectAsState()
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadServiceById(serviceId)
    }

    LaunchedEffect(service) {
        service?.let {
            name = it.title
            description = it.description
            price = it.price
        }
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text("Детали услуги") })
    }) { padding ->
        service?.let {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("ID: ${it.id}", style = MaterialTheme.typography.titleMedium)

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Цена") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(onClick = {
                    viewModel.addService(name, description, price)
                    onSave()
                }) {
                    Text("Сохранить")
                }

                Button(onClick = {
                    viewModel.deleteService(it.id)
                    onSave()
                }) {
                    Text("Удалить")
                }
            }
        } ?: Text("Загрузка...", modifier = Modifier.padding(16.dp))
    }
}
