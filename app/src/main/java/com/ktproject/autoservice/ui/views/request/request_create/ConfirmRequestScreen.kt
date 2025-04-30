package com.ktproject.autoservice.ui.views.request.request_create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ktproject.autoservice.ui.viewmodel.NewRequestViewModel
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmRequestScreen(viewModel: NewRequestViewModel = getViewModel(), onSubmit: () -> Unit) {
    val requestStatus by viewModel.requestStatus.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadServices()
    }

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("Подтверждение заявки") }) }) {
        Column(modifier = Modifier.padding(it.calculateTopPadding() + 16.dp)) {
            requestStatus?.let {
                Text("Ваша заявка на сервис: ${it.serviceId}, описание: ${it.description}")
            }

            Button(onClick = {
                viewModel.createRequest()
                onSubmit()
            }) { Text("Записаться") }
        }
    }
}
