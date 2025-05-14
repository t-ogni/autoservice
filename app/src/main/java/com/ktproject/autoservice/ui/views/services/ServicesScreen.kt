package com.ktproject.autoservice.ui.views.services

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ktproject.autoservice.data.model.Service
import com.ktproject.autoservice.ui.navigation.BottomNavigationBar
import com.ktproject.autoservice.ui.viewmodel.ServicesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesScreen(
    navController: NavHostController,
    onServiceClick: (String) -> Unit,
    viewModel: ServicesViewModel = getViewModel()
) {
    val services by viewModel.services.collectAsState()

    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    fun handleRefresh() {
        isRefreshing = true
        coroutineScope.launch {
            viewModel.loadServices()
            delay(300)
            isRefreshing = false
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Услуги") })
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { handleRefresh() },
            modifier = Modifier.padding(padding).fillMaxSize()
        ) {
            LazyColumn(
                contentPadding = PaddingValues(
                    bottom = 42.dp,
                    start = 8.dp,
                    end = 8.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(services) { service ->
                    ServiceRow(service = service, onClick = { onServiceClick(service.id) })
                }
            }
        }
    }
}

@Composable
fun ServiceRow(service: Service, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = service.title, style = MaterialTheme.typography.bodyLarge)
            Text(text = service.price, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Перейти",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
