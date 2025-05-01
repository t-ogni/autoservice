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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
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

    val formatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val displayFormatter = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val timeSlots = (9..18).map { String.format("%02d:00", it) }
    var expanded by remember { mutableStateOf(false) }
    val selectedTime = requestData.selectedTime ?: ""

    val selectedDateFormatted = requestData.selectedDate?.let {
        try {
            val date = formatter.parse(it)
            displayFormatter.format(date!!)
        } catch (e: Exception) {
            "Неверная дата"
        }
    } ?: ""

    LaunchedEffect(Unit) {
        viewModel.loadBusyDates()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Выбор даты") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = selectedDateFormatted,
                onValueChange = {},
                readOnly = true,
                label = { Text("Выберите дату") },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Открыть календарь")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = selectedTime,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Выберите время") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    trailingIcon = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Открыть список времени")
                        }
                    }
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    timeSlots.forEach { time ->
                        DropdownMenuItem(
                            text = { Text(time) },
                            onClick = {
                                viewModel.updateSelectedTime(time)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val selectedMillis = datePickerState.selectedDateMillis
                    val dateStr = selectedMillis?.let { formatter.format(Date(it)) }

                    if (dateStr != null) {
                        viewModel.updateSelectedDate(dateStr)
                    }

                    viewModel.updateSelectedTime(selectedTime)
                    onNext()
                },
                enabled = requestData.selectedDate != null && requestData.selectedTime != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Далее")
            }

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        val selectedMillis = datePickerState.selectedDateMillis
                        val dateStr = selectedMillis?.let { formatter.format(Date(it)) }

                        val isDateAvailable = dateStr != null && dateStr !in busyDates

                        TextButton(
                            onClick = {
                                if (dateStr != null) {
                                    viewModel.updateSelectedDate(dateStr)
                                }
                                showDatePicker = false
                            },
                            enabled = isDateAvailable
                        ) {
                            Text("ОК")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Отмена")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState, showModeToggle = false)
                }

                // Подсказка при выборе недоступной даты (опционально)
                val selectedMillis = datePickerState.selectedDateMillis
                val dateStr = selectedMillis?.let { formatter.format(Date(it)) }
                if (dateStr != null && dateStr in busyDates) {
                    Text(
                        text = "Эта дата недоступна",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
