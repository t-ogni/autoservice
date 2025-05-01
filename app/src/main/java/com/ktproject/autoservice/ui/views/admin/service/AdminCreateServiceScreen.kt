package com.ktproject.autoservice.ui.views.admin.service

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import com.ktproject.autoservice.ui.viewmodel.AdminServiceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCreateServiceScreen(
    onServiceCreated: () -> Unit,
    viewModel: AdminServiceViewModel = getViewModel()
) {
    val nameState = remember { mutableStateOf("") }
    val descriptionState = remember { mutableStateOf("") }
    val priceState = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Создать услугу") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = nameState.value,
                onValueChange = { nameState.value = it },
                label = { Text("Название") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descriptionState.value,
                onValueChange = { descriptionState.value = it },
                label = { Text("Описание") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = priceState.value,
                onValueChange = { priceState.value = it },
                label = { Text("Цена") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val name = nameState.value.trim()
                    val description = descriptionState.value.trim()
                    val price = priceState.value.trim()

                    if (name.isNotEmpty() && description.isNotEmpty() && price.isNotEmpty()) {
                        viewModel.addService(name, description, price)
                        onServiceCreated()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Создать")
            }
        }
    }
}
