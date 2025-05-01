package com.ktproject.autoservice.ui.views.admin.news

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.ktproject.autoservice.data.repository.ResultState
import com.ktproject.autoservice.ui.components.ErrorSnackbar
import com.ktproject.autoservice.ui.components.UIState
import com.ktproject.autoservice.ui.viewmodel.AdminNewsViewModel
import org.koin.androidx.compose.getViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminEditNewsScreen(
    newsId: String,
    newsViewModel: AdminNewsViewModel = getViewModel(),
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    LaunchedEffect(newsId) {
        newsViewModel.getNewsById(newsId)
    }

    val newsState by newsViewModel.selectedNewsState.collectAsState()

    when (newsState) {
        is UIState.Loading -> {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        }

        is UIState.Error -> {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text("Ошибка загрузки: ${(newsState as UIState.Error).message}")
            }
        }

        is UIState.Success -> {
            val news = (newsState as UIState.Success).data
            val titleState = remember(news.title) { mutableStateOf(TextFieldValue(news.title)) }
            val contentState = remember(news.content) { mutableStateOf(TextFieldValue(news.content)) }

            Scaffold(
                topBar = { CenterAlignedTopAppBar(title = { Text("Редактировать новость") }) }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    Text("Заголовок новости:")
                    TextField(
                        value = titleState.value,
                        onValueChange = { titleState.value = it },
                        modifier = Modifier.padding(bottom = 8.dp),
                        placeholder = { Text("Введите заголовок") }
                    )

                    Text("Содержание новости:")
                    TextField(
                        value = contentState.value,
                        onValueChange = { contentState.value = it },
                        modifier = Modifier.padding(bottom = 8.dp),
                        placeholder = { Text("Введите содержание новости") }
                    )

                    Row(modifier = Modifier.padding(top = 16.dp)) {
                        Button(onClick = {
                            newsViewModel.updateNews(
                                newsId = news.id,
                                title = titleState.value.text,
                                content = contentState.value.text,
                                date = news.date,
                                onSuccess = onSaveClick,
                                onError = { message ->

                                    println("Ошибка обновления: $message")
                                }
                            )
                        }) {
                            Text("Сохранить")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(onClick = onCancelClick) {
                            Text("Отменить")
                        }
                    }
                }
            }
        }
    }
}
