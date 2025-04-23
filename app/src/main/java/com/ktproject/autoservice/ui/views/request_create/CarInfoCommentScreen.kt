package com.ktproject.autoservice.ui.views.request_create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Шаги создания заявки
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarInfoCommentScreen(onNext: () -> Unit) {
    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("Информация об авто") }) }) {
        Column(modifier = Modifier.padding(it.calculateTopPadding() + 16.dp)) {
            TextField(value = "", onValueChange = {}, label = { Text("Марка авто") })
            TextField(value = "", onValueChange = {}, label = { Text("Модель") })
            TextField(value = "", onValueChange = {}, label = { Text("Комментарий") })
            Button(onClick = onNext) { Text("Далее") }
        }
    }
}
