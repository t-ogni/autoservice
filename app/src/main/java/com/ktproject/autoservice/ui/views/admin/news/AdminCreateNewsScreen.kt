package com.ktproject.autoservice.ui.views.admin.news

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.ktproject.autoservice.ui.viewmodel.AdminNewsViewModel
import org.koin.androidx.compose.getViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCreateNewsScreen(
    newsViewModel: AdminNewsViewModel = getViewModel(),
    onAddNews: () -> Unit,
) {
    val title = remember { mutableStateOf(TextFieldValue()) }
    val content = remember { mutableStateOf(TextFieldValue()) }
    val date = remember { mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())) }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Создать новость") }) }
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .padding(16.dp)
        ) {
            Text("Заголовок:")
            TextField(
                value = title.value,
                onValueChange = { title.value = it },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                placeholder = { Text("Введите заголовок") }
            )

            Text("Контент:")
            TextField(
                value = content.value,
                onValueChange = { content.value = it },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                placeholder = { Text("Введите контент") }
            )

            Text("Дата:")
            TextField(
                value = TextFieldValue(date.value),
                onValueChange = { date.value = it.text },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            )

            Button(
                onClick = {
                    newsViewModel.addNews(title.value.text, content.value.text, date.value)
                    onAddNews()
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Добавить новость")
            }
        }
    }
}
