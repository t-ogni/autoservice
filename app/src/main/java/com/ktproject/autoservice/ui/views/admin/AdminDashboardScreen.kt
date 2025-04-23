package com.ktproject.autoservice.ui.views.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onAllRequestsClick: () -> Unit,
    onManageServicesClick: () -> Unit,
    onManageNewsClick: () -> Unit,
    onUsersClick: () -> Unit
) {
    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("Админ-панель") }) }) {
        Column(modifier = Modifier.padding(it.calculateTopPadding() + 16.dp)) {
            Button(onClick = onAllRequestsClick) { Text("Заявки") }
            Button(onClick = onManageServicesClick) { Text("Услуги") }
            Button(onClick = onManageNewsClick) { Text("Новости") }
            Button(onClick = onUsersClick) { Text("Клиенты") }
        }
    }
}