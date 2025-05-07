package com.ktproject.autoservice.ui.views.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.ktproject.autoservice.ui.viewmodel.AuthUiState
import com.ktproject.autoservice.ui.viewmodel.AuthViewModel
import org.koin.androidx.compose.getViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel = getViewModel(),
    onRegisterSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val authState = authViewModel.authUiState.collectAsState().value

    // Навигация по успешной регистрации
    LaunchedEffect(authViewModel.navigationEvent) {
        authViewModel.navigationEvent.collect {
            onRegisterSuccess()
        }
    }

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("Регистрация") }) }) {
        Box(modifier = Modifier.fillMaxSize().padding(
            top = it.calculateTopPadding(),
            bottom = it.calculateBottomPadding(),
            start = 32.dp, end = 32.dp
        ), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f)) // Верхняя 1/3
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    OutlinedTextField(
                        value = name.value,
                        onValueChange = { name.value = it },
                        label = { Text("Имя") },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = email.value,
                        onValueChange = { email.value = it },
                        label = { Text("Email") },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        label = { Text("Пароль") },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Ошибка
                    if (authState is AuthUiState.Error) {
                        Text(
                            text = authState.message,
                            color = androidx.compose.ui.graphics.Color.Red,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    // Кнопка регистрации
                    Button(
                        onClick = {
                            authViewModel.register(name.value.trim(), email.value.trim(), password.value.trim())
                        },
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text("Создать аккаунт")
                    }

                    // Назад
                    TextButton(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Назад")
                    }
                }

                Spacer(modifier = Modifier.weight(3f)) // Нижняя 2/3
            }
        }
    }
}
