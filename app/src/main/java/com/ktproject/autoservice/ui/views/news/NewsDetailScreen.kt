package com.ktproject.autoservice.ui.views.news


import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.viewinterop.AndroidView
import com.ktproject.autoservice.ui.components.UIState
import com.ktproject.autoservice.ui.viewmodel.NewsDetailViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailScreen(
    newsId: String,
    viewModel: NewsDetailViewModel = getViewModel()
) {
    val state by viewModel.newsState.collectAsState()

    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    fun handleRefresh() {
        isRefreshing = true
        coroutineScope.launch {
            viewModel.loadNews(newsId)
            delay(300)
            isRefreshing = false
        }
    }

    LaunchedEffect(newsId) {
        viewModel.loadNews(newsId)
    }

    when (state) {
        is UIState.Loading ->  {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }
        is UIState.Success -> {
            val news = (state as UIState.Success).data
    Scaffold { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)
        ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(text = news.title, style = MaterialTheme.typography.headlineSmall)
                        Text(text = news.date, style = MaterialTheme.typography.labelMedium)

                        AndroidView(
                            factory = { context ->
                                WebView(context).apply {
                                    webViewClient = WebViewClient()
                                    loadDataWithBaseURL(
                                        null,
                                        news.content,
                                        "text/html",
                                        "utf-8",
                                        null
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

            }
        }
        is UIState.Error -> {
            Scaffold (modifier = Modifier.fillMaxSize().systemBarsPadding()){ padding ->
                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = { handleRefresh() },
                    modifier = Modifier.padding(padding)
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
}
