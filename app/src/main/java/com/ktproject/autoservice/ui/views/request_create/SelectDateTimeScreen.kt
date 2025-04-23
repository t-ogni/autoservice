package com.ktproject.autoservice.ui.views.request_create

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Шаги создания заявки
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDateTimeScreen(onNext: () -> Unit) {
    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("Выбор сервиса") }) })
    {
        Column(modifier = Modifier.padding(it.calculateTopPadding() + 16.dp)) {
            // TODO: список услуг с чекбоксами
            Button(onClick = onNext) { Text("Далее") }
        }
    }
}