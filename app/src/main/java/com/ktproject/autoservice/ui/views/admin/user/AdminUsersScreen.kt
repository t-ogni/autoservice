package com.ktproject.autoservice.ui.views.admin.user

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ktproject.autoservice.ui.viewmodel.AdminUsersViewModel
import org.koin.androidx.compose.getViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ktproject.autoservice.data.model.User
import com.ktproject.autoservice.ui.components.UIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsersScreen(
    onUserClick: (String) -> Unit,
    viewModel: AdminUsersViewModel = getViewModel()
) {
    val usersState by viewModel.usersState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAllUsers()
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Клиенты") }) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (usersState) {
                is UIState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is UIState.Error -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text((usersState as UIState.Error).message, color = MaterialTheme.colorScheme.error)
                    }
                }
                is UIState.Success -> {
                    val users = (usersState as UIState.Success<List<User>>).data

                    LazyColumn(modifier = Modifier.padding(16.dp)) {
                        items(users) { user ->
                            UserItem(user = user, onClick = { onUserClick(user.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(user: User, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = user.name, style = MaterialTheme.typography.titleMedium)
            Text(text = user.email, style = MaterialTheme.typography.bodyMedium)
        }
    }
}