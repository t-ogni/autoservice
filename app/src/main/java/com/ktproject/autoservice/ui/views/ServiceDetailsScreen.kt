package com.ktproject.autoservice.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ktproject.autoservice.data.model.Service

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailsScreen(
    serviceId: String,
    navController: NavController // если используешь навигацию
) {
    val service: Service? = null

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(service?.title ?: "Услуга") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Image(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
//        service?.let {
//            Column(
//                modifier = Modifier
//                    .padding(padding)
//                    .padding(16.dp)
//                    .fillMaxSize(),
//                verticalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//
//                Text(text = it.title, style = androidx.compose.material3.MaterialTheme.typography.headlineSmall)
//                Text(text = it.description)
//            }
//        } ?: run {
            Text("Услуга не найдена", modifier = Modifier.padding(16.dp))
//        }
    }
}
