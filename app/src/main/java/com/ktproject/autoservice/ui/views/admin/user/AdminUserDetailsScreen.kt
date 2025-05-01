package com.ktproject.autoservice.ui.views.admin.user

import com.ktproject.autoservice.ui.viewmodel.UserViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ktproject.autoservice.data.model.User
import com.ktproject.autoservice.ui.components.UIState
import com.ktproject.autoservice.ui.viewmodel.EditUserViewModel
import org.koin.androidx.compose.getViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUserDetailsScreen(
    userId: String,
    viewModel: EditUserViewModel = getViewModel()
) {
    val userState by viewModel.userState.collectAsState()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("user") }

    var expanded by remember { mutableStateOf(false) }
    val roles = listOf("user", "admin")

    LaunchedEffect(userId) {
        viewModel.loadUser(userId)
    }

    LaunchedEffect(userState) {
        if (userState is UIState.Success) {
            val user = (userState as UIState.Success<User?>).data
            user?.let {
                name = it.name
                email = it.email
                role = it.role ?: "user"
            }
        }
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Детали клиента") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when (userState) {
                is UIState.Loading -> CircularProgressIndicator()
                is UIState.Error -> Text(
                    (userState as UIState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )

                is UIState.Success -> {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Имя") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Выпадающий список для роли
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = role,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Роль") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            roles.forEach { selection ->
                                DropdownMenuItem(
                                    text = { Text(selection) },
                                    onClick = {
                                        role = selection
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {
                            viewModel.updateUser(userId, name, email, role)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Сохранить")
                    }

                    Button(
                        onClick = {
                            viewModel.deleteUser(userId) {
                                // handle deletion callback (например, перейти назад)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Удалить")
                    }
                }
            }
        }
    }
}
