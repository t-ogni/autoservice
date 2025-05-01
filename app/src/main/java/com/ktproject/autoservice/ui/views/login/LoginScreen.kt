package com.ktproject.autoservice.ui.views.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.ktproject.autoservice.ui.viewmodel.AuthViewModel
import com.ktproject.autoservice.ui.viewmodel.AuthUiState
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = getViewModel(),
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    // Состояния для ввода данных
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val loginError = remember { mutableStateOf("") }

    val uiState by authViewModel.authUiState.collectAsState()

    LaunchedEffect (Unit) {
        authViewModel.navigationEvent.collect {
            onLoginSuccess()
        }
    }

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("Вход") }) }) {
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

                    // Поле для ввода Email
                    OutlinedTextField(
                        value = email.value,
                        onValueChange = { email.value = it },
                        label = { Text("Email") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { /* Focus переключается на следующее поле */ }
                        ),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    )

                    // Поле для ввода Пароля
                    OutlinedTextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        label = { Text("Пароль") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                // Логиним пользователя
                                authViewModel.login(email.value.trim(), password.value.trim())
                            }
                        ),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    )


                    // Отображение ошибки логина
                    if (loginError.value.isNotEmpty()) {
                        Text(text = loginError.value, color = Color.Red)
                    }


                    when (uiState) {
                        is AuthUiState.Loading -> {
                            CircularProgressIndicator()
                        }

                        is AuthUiState.Error -> {
                            Text(
                                text = (uiState as AuthUiState.Error).message,
                                color = Color.Red
                            )
                        }

                        else -> {

                        }
                    }
                    // Кнопка входа
                    Button(onClick = {
                        authViewModel.login(email.value.trim(), password.value.trim())
                    }) {
                        Text("Войти")
                    }

                    // Кнопка регистрации
                    TextButton(onClick = onRegisterClick) {
                        Text("Регистрация")
                    }
                }

                Spacer(modifier = Modifier.weight(2f)) // Нижняя 2/3
            }
        }
    }
}