package com.ktproject.autoservice.ui.views

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavHostController
import com.ktproject.autoservice.data.model.News
import com.ktproject.autoservice.data.model.Request
import com.ktproject.autoservice.ui.components.UIState
import com.ktproject.autoservice.ui.navigation.BottomNavigationBar
import com.ktproject.autoservice.ui.viewmodel.HomeUiState
import com.ktproject.autoservice.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = getViewModel(),
    onCreateRequestClick: () -> Unit,
    onRequestClick: (String) -> Unit,
    onNewsClick: (String) -> Unit,
    onAdminPanelClick: () -> Unit,
) {

    val requestsUiState by viewModel.requestsUiState.collectAsState()
    val newsUiState by viewModel.newsUiState.collectAsState()
    val userRole by viewModel.userRole.collectAsState()



    var backPressedOnce by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = context as? Activity

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val shouldReload = savedStateHandle?.get<Boolean>("newRequestCreated") ?: false

    if (shouldReload) {
        savedStateHandle?.set("newRequestCreated", false)
    }

    LaunchedEffect(Unit) {
        viewModel.loadHomeData()
    }

    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    fun handleRefresh() {
        isRefreshing = true
        coroutineScope.launch {
            viewModel.loadHomeData()
            delay(300)
            isRefreshing = false
        }
    }

    when (userRole) {
        is UIState.Loading -> {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(title = { Text("Главное меню") })
                },
                bottomBar = {
                    BottomNavigationBar(navController = navController)
                }
            ) { padding ->

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize().padding(padding)
                    ) {
                        CircularProgressIndicator()
                    }

            }
        }

        is UIState.Success -> {
            val userRoleString = (userRole as UIState.Success<String>).data
            Scaffold(
                topBar = {
                    if (userRoleString == "admin") {
                        TopAppBar(
                            title = { Text("Главное меню") },
                            actions = {
                                IconButton(onClick = { onAdminPanelClick() }) {
                                    Icon(
                                        imageVector = Icons.Default.Build,
                                        contentDescription = "Админ-панель"
                                    )
                                }
                            }
                        )
                    } else {
                        CenterAlignedTopAppBar(
                            title = { Text("Главное меню") }
                        )
                    }
                },
                bottomBar = {
                    BottomNavigationBar(navController = navController)
                }
            ) { padding ->
                PullToRefreshBox(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    isRefreshing = isRefreshing,
                    onRefresh = { handleRefresh() }
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        item {
                            when (requestsUiState) {
                                is UIState.Loading -> {
                                    Box(
                                        Modifier.fillMaxWidth().height(100.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }

                                is UIState.Success -> {
                                    val requests =
                                        (requestsUiState as UIState.Success<List<Request>>).data
                                    if (requests.isEmpty()) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(100.dp)
                                        ) {
                                            Card(
                                                onClick = onCreateRequestClick,
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(horizontal = 16.dp)
                                            ) {
                                                Box(
                                                    contentAlignment = Alignment.Center,
                                                    modifier = Modifier.fillMaxSize()
                                                ) {
                                                    Text(
                                                        text = "Создать заявку (+)",
                                                        style = MaterialTheme.typography.bodyLarge
                                                    )
                                                }
                                            }
                                        }
                                    } else {
                                        LazyRow(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(100.dp)
                                        ) {
                                            items(requests) { request ->
                                                Card(
                                                    onClick = { onRequestClick(request.id) },
                                                    modifier = Modifier
                                                        .width(120.dp)
                                                        .fillMaxHeight()
                                                ) {
                                                    Box(
                                                        contentAlignment = Alignment.Center,
                                                        modifier = Modifier.fillMaxSize()
                                                    ) {
                                                        Text(
                                                            text = request.description,
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            modifier = Modifier.padding(8.dp)
                                                        )
                                                    }
                                                }
                                            }
                                            item {
                                                Card(
                                                    onClick = onCreateRequestClick,
                                                    modifier = Modifier
                                                        .width(120.dp)
                                                        .fillMaxHeight()
                                                ) {
                                                    Box(
                                                        contentAlignment = Alignment.Center,
                                                        modifier = Modifier.fillMaxSize()
                                                    ) {
                                                        Text(
                                                            text = "+",
                                                            style = MaterialTheme.typography.headlineMedium
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }

                                is UIState.Error -> {
                                    Box(
                                        Modifier.fillMaxWidth().height(100.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("Ошибка загрузки заявок")
                                    }
                                }
                            }
                        }
                        item {
                            Text(
                                text = "Новости и акции",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        when (newsUiState) {
                            is UIState.Loading -> {
                                item {
                                    Box(
                                        Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }

                            is UIState.Success -> {
                                val newsList = (newsUiState as UIState.Success<List<News>>).data
                                items(newsList) { news ->
                                    Card(
                                        onClick = { onNewsClick(news.id) },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Box(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                                            Row {

                                                Text(
                                                    text = news.title,
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                Column(
                                                    modifier = Modifier.widthIn(50.dp, 100.dp),
                                                    horizontalAlignment = Alignment.End
                                                ) {
                                                    Text(
                                                        text = news.date,
                                                        style = MaterialTheme.typography.bodySmall
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            is UIState.Error -> {
                                item {
                                    Box(
                                        Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("Ошибка загрузки новостей")
                                    }
                                }
                            }
                        }
                    }
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
                        Text("Ошибка загрузки пользователя")
                    }
                }
            }
        }
    }


    BackHandler {
        if (backPressedOnce) {
            activity?.finish()
        } else {
            backPressedOnce = true
            Toast.makeText(context, "Нажмите ещё раз для выхода", Toast.LENGTH_SHORT).show()
            coroutineScope.launch {
                delay(2000)
                backPressedOnce = false
            }
        }
    }
}
