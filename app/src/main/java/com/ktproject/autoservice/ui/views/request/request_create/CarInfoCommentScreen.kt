package com.ktproject.autoservice.ui.views.request.request_create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ktproject.autoservice.ui.viewmodel.CarSearchViewModel
import com.ktproject.autoservice.ui.viewmodel.NewRequestViewModel
import org.koin.androidx.compose.getViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarInfoCommentScreen(
    viewModel: NewRequestViewModel = getViewModel(),
    carSearchViewModel: CarSearchViewModel = getViewModel(),
    onNext: () -> Unit
) {
    val requestData by viewModel.requestData.collectAsState()
    val makeQuery = carSearchViewModel.makeQuery
    val makeSuggestions = carSearchViewModel.makeSuggestions

    val carQuery = carSearchViewModel.query
    val carSuggestions = carSearchViewModel.suggestions

    var makeExpanded by remember { mutableStateOf(false) }
    var modelExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Информация об авто") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .padding(24.dp)
        ) {
            // Поиск марки
            TextField(
                value = makeQuery,
                onValueChange = {
                    carSearchViewModel.onMakeQueryChange(it)
                    makeExpanded = true
                },
                label = { Text("Марка авто") },
                modifier = Modifier.fillMaxWidth()
            )

            DropdownMenu(
                expanded = makeExpanded && makeSuggestions.isNotEmpty(),
                onDismissRequest = { makeExpanded = false }
            ) {
                makeSuggestions.forEach { make ->
                    DropdownMenuItem(
                        text = { Text(make.Make_Name) },
                        onClick = {
                            carSearchViewModel.selectMake(make)
                            makeExpanded = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.padding(8.dp))

            // Поиск модели
            TextField(
                value = carQuery,
                onValueChange = {
                    carSearchViewModel.onCarQueryChange(it)
                    modelExpanded = true
                },
                label = { Text("Модель авто") },
                modifier = Modifier.fillMaxWidth()
            )

            DropdownMenu(
                expanded = modelExpanded && carSuggestions.isNotEmpty(),
                onDismissRequest = { modelExpanded = false }
            ) {
                carSuggestions.forEach { suggestion ->
                    DropdownMenuItem(
                        text = { Text(suggestion) },
                        onClick = {
                            carSearchViewModel.selectSuggestion(suggestion)
                            modelExpanded = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.padding(8.dp))

            // Комментарий
            TextField(
                value = requestData.comment,
                onValueChange = {
                    // Обновляем модель, включая выбранную машину
                    viewModel.updateCarInfo(
                        carSearchViewModel.query,
                        it
                    )
                },
                label = { Text("Комментарий") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Далее")
            }
        }
    }
}
