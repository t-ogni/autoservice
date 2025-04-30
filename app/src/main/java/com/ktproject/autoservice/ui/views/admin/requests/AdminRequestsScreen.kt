package com.ktproject.autoservice.ui.views.admin.requests

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ktproject.autoservice.data.model.Request
import com.ktproject.autoservice.ui.components.UIState
import com.ktproject.autoservice.ui.viewmodel.AdminRequestsViewModel
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminRequestsScreen(
    onRequestClick: (String) -> Unit,
    viewModel: AdminRequestsViewModel = getViewModel()
) {
    val state by viewModel.requestsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAllRequests()
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text("Все заявки") })
    }) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            when (state) {
                is UIState.Loading -> CircularProgressIndicator()
                is UIState.Error -> Text((state as UIState.Error).message)
                is UIState.Success -> {
                    val requests = (state as UIState.Success<List<Request>>).data
                    LazyColumn {
                        items(requests) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { onRequestClick(it.id) }
                            ) {
                                Column(Modifier.padding(16.dp)) {
                                    Text("ID: ${it.id}")
                                    Text("Услуга: ${it.serviceId}")
                                    Text("Статус: ${it.status}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
