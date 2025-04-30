package com.ktproject.autoservice.ui.views.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onAllRequestsClick: () -> Unit,
    onManageServicesClick: () -> Unit,
    onManageNewsClick: () -> Unit,
    onUsersClick: () -> Unit
) {
    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("Админ-панель") }) }) {
        Column(modifier = Modifier.padding(top = it.calculateTopPadding(), end = 16.dp, start = 16.dp)) {
            // Кнопки с отступами и стильными закруглениями
            Button(
                onClick = onAllRequestsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 12.dp).height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Заявки")
            }

            Button(
                onClick = onManageServicesClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 12.dp).height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Услуги", fontWeight = FontWeight.ExtraBold)
            }

            Button(
                onClick = onManageNewsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 12.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Новости", fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = onUsersClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp).height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Клиенты")
            }
        }
    }
}
