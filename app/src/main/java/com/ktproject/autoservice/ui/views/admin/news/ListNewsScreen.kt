package com.ktproject.autoservice.ui.views.admin.news

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ktproject.autoservice.data.model.News
import com.ktproject.autoservice.data.repository.RepositoryResult
import com.ktproject.autoservice.ui.components.UIState
import com.ktproject.autoservice.ui.viewmodel.AdminNewsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListNewsScreen(
    newsViewModel: AdminNewsViewModel = getViewModel(),
    onAddNewsClick: () -> Unit,
    onEditNewsClick: (String) -> Unit,
) {
    val newsState by newsViewModel.newsListState.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    fun handleRefresh() {
        isRefreshing = true
        coroutineScope.launch {
            newsViewModel.loadNews()
            delay(300)
            isRefreshing = false
        }
    }

    LaunchedEffect(Unit) {
        newsViewModel.loadNews()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Новости") },
                actions = {
                    IconButton(onClick = onAddNewsClick) {
                        Text("+", style = MaterialTheme.typography.headlineMedium)
                    }
                }
            )
        }
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { handleRefresh() },
            modifier = Modifier.padding(padding)
        ) {
            when (newsState) {
                is UIState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is UIState.Error -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Text((newsState as UIState.Error).message)
                        }
                    }
                }

                is UIState.Success -> {
                    val newsList = (newsState as UIState.Success<List<News>>).data
                    LazyColumn(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        items(newsList) { news ->
                            NewsItem(
                                news = news,
                                onEditClick = { onEditNewsClick(news.id) },
                                onDeleteClick = { newsViewModel.deleteNews(news.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NewsItem(news: News, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(news.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(news.content, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(news.date, style = MaterialTheme.typography.bodySmall, color = Color.Gray)

            Row(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Button(onClick = onEditClick) {
                    Text("Редактировать")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onDeleteClick, colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                )) {
                    Text("Удалить")
                }
            }
        }
    }
}
