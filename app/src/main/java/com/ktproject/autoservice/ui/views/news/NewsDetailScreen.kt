package com.ktproject.autoservice.ui.views.news

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
fun NewsDetailScreen(newsId: String) {
    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("Новость #$newsId") }) }) {
        Column(modifier = Modifier.padding(it.calculateTopPadding() + 16.dp)) {
        }
    }
}