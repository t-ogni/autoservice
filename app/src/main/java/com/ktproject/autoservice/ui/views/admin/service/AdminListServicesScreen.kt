package com.ktproject.autoservice.ui.views.admin.service

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import com.ktproject.autoservice.data.model.Request
import com.ktproject.autoservice.data.model.Service
import com.ktproject.autoservice.ui.components.UIState
import com.ktproject.autoservice.ui.viewmodel.AdminRequestsViewModel
import com.ktproject.autoservice.ui.viewmodel.AdminServiceViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminListServicesScreen(
    onServiceClick: (String) -> Unit,
    onServiceAddClick: () -> Unit,
    viewModel: AdminServiceViewModel = getViewModel()
) {
    val state by viewModel.servicesState.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    fun handleRefresh() {
        isRefreshing = true
        coroutineScope.launch {
            viewModel.loadAllServices()
            delay(300)
            isRefreshing = false
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadAllServices()
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = { Text("Список услуг") },
            actions = {
                IconButton(onClick = onServiceAddClick) {
                    Text("+", style = MaterialTheme.typography.headlineMedium)
                }
            })
    }) { padding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { handleRefresh() },
            modifier = Modifier.padding(padding)
        ) {
            Column(Modifier.padding(16.dp)) {
                when (state) {
                    is UIState.Loading -> CircularProgressIndicator()
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
                        val services = (state as UIState.Success<List<Service>>).data
                        LazyColumn {
                            items(services) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable { onServiceClick(it.id) }
                                ) {
                                    Column(Modifier.padding(16.dp)) {
                                        Text("ID: ${it.id}")
                                        Text("Название: ${it.title}")
                                        Text("Цена: ${it.price}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
