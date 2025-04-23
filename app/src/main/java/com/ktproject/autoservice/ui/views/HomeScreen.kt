package com.ktproject.autoservice.ui.views

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    var backPressedOnce by remember { mutableStateOf(false) }

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("Главное меню") }) }) {
        Column(modifier = Modifier.padding(it.calculateTopPadding() + 16.dp)) {
        Button(onClick = onCreateRequestClick) { Text("Записаться на ТО") }
            Button(onClick = onMyRequestsClick) { Text("Мои заявки") }
            Button(onClick = onServicesClick) { Text("Услуги") }
            Button(onClick = onNewsClick) { Text("Новости и акции") }
            Button(onClick = onProfileClick) { Text("Профиль") }
            Button(onClick = onAdminPanelClick) { Text("Админ-панель") }
        }
    }

    val context = LocalContext.current
    val activity = context as? Activity
    val coroutineScope = rememberCoroutineScope()

    BackHandler {
        if (backPressedOnce) {
            activity?.finish()
        } else {
            backPressedOnce = true
            Toast.makeText(context, "Нажмите ещё раз для выхода", Toast.LENGTH_SHORT).show()
            coroutineScope.launch {
                delay(2000)
                backPressedOnce = false
            }
        }
    }
}