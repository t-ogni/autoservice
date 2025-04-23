package com.ktproject.autoservice.ui.views.login

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

// Шаблон регистрации
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("Регистрация") }) })
        {
        Column(modifier = Modifier.padding(it.calculateTopPadding() + 16.dp)) {
            TextField(value = "", onValueChange = {}, label = { Text("Имя") })
            TextField(value = "", onValueChange = {}, label = { Text("Email") })
            TextField(value = "", onValueChange = {}, label = { Text("Пароль") })
            Button(onClick = onRegisterSuccess) { Text("Создать аккаунт") }
            TextButton(onClick = onBackClick) { Text("Назад") }
        }
    }
}