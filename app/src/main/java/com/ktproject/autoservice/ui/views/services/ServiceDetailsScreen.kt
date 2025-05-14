package com.ktproject.autoservice.ui.views.services

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ktproject.autoservice.data.model.Service
import com.ktproject.autoservice.ui.components.ErrorSnackbar
import com.ktproject.autoservice.ui.components.UIState
import com.ktproject.autoservice.ui.viewmodel.ServicesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailsScreen(
    viewModel: ServicesViewModel = getViewModel(),
    serviceId: String,
    onCreateRequest: (String) -> Unit = {}, // обработчик нажатия "Оставить заявку"
    onBackClick: () -> Unit = {} // обработчик нажатия "Оставить заявку"
) {

    val state by viewModel.serviceState.collectAsState()

    LaunchedEffect(serviceId) {
        viewModel.loadServiceById(serviceId)
    }

    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    fun handleRefresh() {
        isRefreshing = true
        coroutineScope.launch {
            viewModel.loadServiceById(serviceId)
            delay(300)
            isRefreshing = false
        }
    }

    when (state) {
        is UIState.Loading -> {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text("Услуга загружается...") },
                        navigationIcon = {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Назад"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }) { paddingValues ->
                Surface(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }

        }
        is UIState.Success -> {
            val service = (state as UIState.Success<Service>).data

            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text(service.title) },
                        navigationIcon = {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Назад"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                },
                bottomBar = {
                    Button(
                        onClick = { onCreateRequest(service.id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .systemBarsPadding()
                            .padding(16.dp),
                        shape = ShapeDefaults.Small
                    ) {
                        Text("Оставить заявку")
                    }
                }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
//                AsyncImage(
//                    model = service.imageUrl,
//                    contentDescription = service.title,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .aspectRatio(16f / 9f)
//                )
                    Text(
                        text = service.title,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = service.description,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
        is UIState.Error -> {
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = { handleRefresh() }
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Text((state as UIState.Error).message)
                    }
                }
            }
        }
    }


}

