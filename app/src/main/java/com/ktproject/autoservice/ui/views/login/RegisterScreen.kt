package com.ktproject.autoservice.ui.views.login

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.ktproject.autoservice.ui.viewmodel.AuthViewModel
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel = getViewModel(),
    onRegisterSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    // Состояния для ввода данных
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val registrationError = remember { mutableStateOf("") }

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("Регистрация") }) }) {
        Column(modifier = Modifier.padding(it.calculateTopPadding() + 16.dp)) {

            // Поле для ввода имени
            OutlinedTextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = { Text("Имя") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { /* Focus переключается на следующее поле */ }
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

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
                        // Регистрация
                        authViewModel.login(email.value, password.value)
                    }
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Отображение ошибки регистрации
            if (registrationError.value.isNotEmpty()) {
                Text(text = registrationError.value, color = androidx.compose.ui.graphics.Color.Red)
            }

            // Кнопка для регистрации
            Button(onClick = {
                authViewModel.login(email.value, password.value)
            }) {
                Text("Создать аккаунт")
            }

            // Кнопка для возврата
            TextButton(onClick = onBackClick) {
                Text("Назад")
            }
        }
    }
}
