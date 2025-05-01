package com.ktproject.autoservice.ui.views.request.request_create

import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ktproject.autoservice.ui.viewmodel.NewRequestViewModel
import org.koin.androidx.compose.getViewModel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import com.ktproject.autoservice.data.model.Service
import com.ktproject.autoservice.ui.components.UIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectServiceScreen(
    viewModel: NewRequestViewModel = getViewModel(),
    onNext: () -> Unit,
    preselectedServiceId: String? = null // Передаваемый параметр
) {
    val servicesState by viewModel.servicesState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadServices(preselectedServiceId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Выбор услуги") })
        },
        bottomBar = {
            if (servicesState is UIState.Success && (servicesState as UIState.Success<List<Service>>).data.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = onNext,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Далее")
                    }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier
            .padding(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding(),
                start = 18.dp,
                end = 18.dp
            )
            .fillMaxSize()
        ) {
            when (servicesState) {
                is UIState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is UIState.Error -> {
                    Text(
                        text = "Ошибка загрузки",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is UIState.Success -> {
                    val services = (servicesState as UIState.Success<List<Service>>).data
                    val selectedServiceId = viewModel.requestData.collectAsState().value.selectedServiceId

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(services) { service ->
                            val isSelected = selectedServiceId == service.id

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .background(
                                        color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                        else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { viewModel.updateSelectedService(service.id) }
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = service.title,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.weight(1f)
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                        item { Spacer(modifier = Modifier.height(80.dp)) } // чтобы контент не перекрывался кнопкой
                    }
                }
            }
        }
    }
}
