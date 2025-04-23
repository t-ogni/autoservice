package com.ktproject.autoservice.ui.views

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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Главная
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onCreateRequestClick: () -> Unit,
    onMyRequestsClick: () -> Unit,
    onServicesClick: () -> Unit,
    onNewsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onAdminPanelClick: () -> Unit
) {
    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("Вход") }) }) {
        Column(modifier = Modifier.padding(it.calculateTopPadding() + 16.dp)) {
        Button(onClick = onCreateRequestClick) { Text("Записаться на ТО") }
            Button(onClick = onMyRequestsClick) { Text("Мои заявки") }
            Button(onClick = onServicesClick) { Text("Услуги") }
            Button(onClick = onNewsClick) { Text("Новости и акции") }
            Button(onClick = onProfileClick) { Text("Профиль") }
            Button(onClick = onAdminPanelClick) { Text("Админ-панель") }
        }
    }
}