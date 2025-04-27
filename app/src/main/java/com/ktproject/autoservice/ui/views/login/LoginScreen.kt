package com.ktproject.autoservice.ui.views.login

import android.widget.Toast
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.ktproject.autoservice.ui.viewmodel.AuthViewModel
import com.ktproject.autoservice.ui.viewmodel.UiState
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

    val uiState by authViewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect (Unit) {
        authViewModel.navigationEvent.collect {
            onLoginSuccess()
        }
    }

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("Вход") }) }) {
        Column(modifier = Modifier.padding(it.calculateTopPadding() + 16.dp)) {

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
                modifier = Modifier.padding(bottom = 16.dp)
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
                        authViewModel.login(email.value, password.value)
                    }
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )


            // Отображение ошибки логина
            if (loginError.value.isNotEmpty()) {
                Text(text = loginError.value, color = Color.Red)
            }


            when (uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator()
                }

                is UiState.Error -> {
                    Text(
                        text = (uiState as UiState.Error).message,
                        color = Color.Red
                    )
                }
                else -> {

                }
            }
            // Кнопка входа
            Button(onClick = {
                authViewModel.login(email.value, password.value)
            }) {
                Text("Войти")
            }

            // Кнопка регистрации
            TextButton(onClick = onRegisterClick) {
                Text("Регистрация")
            }
        }
    }
}