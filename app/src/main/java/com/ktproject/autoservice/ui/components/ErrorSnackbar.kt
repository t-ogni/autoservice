package com.ktproject.autoservice.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ErrorSnackbar(
    message: String,
    onRetry: (() -> Unit)? = null
) {
    Snackbar(
        modifier = Modifier.padding(8.dp),
        containerColor = MaterialTheme.colorScheme.error,
        action = {
            if (onRetry != null) {
                TextButton(onClick = onRetry) {
                    Text("Повторить", color = MaterialTheme.colorScheme.onError)
                }
            }
        }
    ) {
        Text(text = message, color = MaterialTheme.colorScheme.onError)
    }
}
