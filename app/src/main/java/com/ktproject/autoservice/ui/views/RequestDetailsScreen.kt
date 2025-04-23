package com.ktproject.autoservice.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestDetailsScreen(requestId: String) {
    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("Заявка #$requestId") }) }) {
        Column(modifier = Modifier.padding(it.calculateTopPadding() + 16.dp)) {
        }
    }
}