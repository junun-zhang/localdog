package com.calsync.app.ui.task

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TasksScreen(
    viewModel: TaskViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 2.dp
        ) {
            Text(
                text = "待办事项",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                        .padding(16.dp),
                    onClick = { }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("暂无待办", fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("点击右下角 + 添加待办事项",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}
