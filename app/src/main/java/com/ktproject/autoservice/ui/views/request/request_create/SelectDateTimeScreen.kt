package com.ktproject.autoservice.ui.views.request.request_create

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.ktproject.autoservice.ui.viewmodel.NewRequestViewModel
import org.koin.androidx.compose.getViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.Alignment
import com.vanpra.composematerialdialogs.*
import com.vanpra.composematerialdialogs.datetime.date.datepicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDateTimeScreen(
    viewModel: NewRequestViewModel = getViewModel(),
    onNext: () -> Unit
) {
    val requestData by viewModel.requestData.collectAsState()
    val busyDates by viewModel.busyDates.collectAsState(initial = emptyList())

    val dateDialogState = rememberMaterialDialogState()

    LaunchedEffect(Unit) { viewModel.loadBusyDates() }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text("Выбор даты") })
    }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val formattedDateText = remember(requestData.selectedDate) {
                requestData.selectedDate?.let {
                    try {
                        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val date = parser.parse(it)
                        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                        date?.let { d -> "Выбрано: ${formatter.format(d)}" } ?: "Выбрать дату"
                    } catch (e: Exception) {
                        "Неверная дата"
                    }
                } ?: "Выбрать дату"
            }

            Button(
                onClick = { dateDialogState.show() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = formattedDateText)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onNext,
                enabled = requestData.selectedDate != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Далее")
            }
        }
    }

    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton("Ок")
            negativeButton("Отмена")
        }
    ) {
        datepicker(
            allowedDateValidator = { date ->
                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dateString = formatter.format(date)
                dateString !in busyDates
            }
        ) { date ->
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            viewModel.updateSelectedDate(formatter.format(date))
        }
    }
}
