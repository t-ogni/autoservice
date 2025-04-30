package com.ktproject.autoservice.ui.views.admin.news

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import com.ktproject.autoservice.data.model.News
import com.ktproject.autoservice.ui.viewmodel.NewsViewModel
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListNewsScreen(
    newsViewModel: NewsViewModel = getViewModel(),
    onAddNewsClick: () -> Unit,
    onEditNewsClick: (String) -> Unit,
) {
    val newsList by newsViewModel.newsList.collectAsState(emptyList())

    LaunchedEffect(Unit) {
        newsViewModel.loadNews()
    }

    newsList.forEach {
        Log.d("ListNewsScreen", "News id = ${it.id}, title = ${it.title}")
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
        LazyColumn(
            modifier = Modifier
                .padding(padding)
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
