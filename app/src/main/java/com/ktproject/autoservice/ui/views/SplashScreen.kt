package com.ktproject.autoservice.ui.views


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ktproject.autoservice.data.model.Service
import com.ktproject.autoservice.ui.navigation.BottomNavigationBar
import com.ktproject.autoservice.ui.viewmodel.ServicesViewModel
import com.ktproject.autoservice.ui.viewmodel.SplashViewModel
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.navigation.NavController


@Composable
fun SplashScreen(
    viewModel: SplashViewModel = getViewModel(), // через Koin
    onAuthSuccess: () -> Unit,
    onAuthFailed: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.observeState { state ->
            when (state) {
                is SplashViewModel.SplashState.Authenticated -> onAuthSuccess()
                is SplashViewModel.SplashState.Unauthenticated -> onAuthFailed()
            }
        }
        viewModel.checkAuth()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
