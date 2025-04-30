package com.ktproject.autoservice.ui.views.request.request_create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ktproject.autoservice.ui.viewmodel.NewRequestViewModel
import org.koin.androidx.compose.getViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarInfoCommentScreen(
    viewModel: NewRequestViewModel = getViewModel(),
    onNext: () -> Unit
) {
    val requestData by viewModel.requestData.collectAsState()

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("Информация об авто") }) }) { padding ->
        Column(modifier = Modifier.padding(padding).padding(24.dp)) {
            TextField(
                value = requestData.carBrand,
                onValueChange = { viewModel.updateCarInfo(it, requestData.carModel, requestData.comment) },
                label = { Text("Марка авто") },
                modifier = Modifier.padding(bottom = 16.dp)
            )

            TextField(
                value = requestData.carModel,
                onValueChange = { viewModel.updateCarInfo(requestData.carBrand, it, requestData.comment) },
                label = { Text("Модель авто") },
                modifier = Modifier.padding(bottom = 16.dp)
            )

            TextField(
                value = requestData.comment,
                onValueChange = { viewModel.updateCarInfo(requestData.carBrand, requestData.carModel, it) },
                label = { Text("Комментарий") },
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Далее")
            }
        }
    }
}
