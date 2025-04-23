package com.ktproject.autoservice.ui.views

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
import androidx.navigation.NavHostController
import com.ktproject.autoservice.ui.navigation.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController, onLogout: () -> Unit) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text("Профиль") })
    }, bottomBar = {
        BottomNavigationBar(navController = navController)
    }
    ) {
        Column(modifier = Modifier.padding(it.calculateTopPadding() + 16.dp)) {
            Button(onClick = onLogout) { Text("Выход") }
        }
    }
}