package com.ktproject.autoservice.ui.views

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.ktproject.autoservice.ui.navigation.BottomNavigationBar
import com.ktproject.autoservice.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = viewModel(),
    onCreateRequestClick: () -> Unit,
    onRequestClick: (String) -> Unit,
    onNewsClick: (String) -> Unit,
    onAdminPanelClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var backPressedOnce by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = context as? Activity
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Главное меню") })
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (uiState.myRequests.isEmpty()) {
                Button(
                    onClick = onCreateRequestClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text("Создать заявку (+)")
                }
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                ) {
                    items(uiState.myRequests) { request ->
                        Card(
                            onClick = { onRequestClick(request.id) },
                            modifier = Modifier
                                .width(120.dp)
                                .fillMaxHeight()
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
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
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Text(
                                    text = "+",
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Новости и акции",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.newsList) { news ->
                    Card(
                        onClick = { onNewsClick(news.id) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(modifier = Modifier.padding(16.dp)) {
                            Text(text = news.title, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }

            if (uiState.userRole == "admin") {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onAdminPanelClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Админ-панель")
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
