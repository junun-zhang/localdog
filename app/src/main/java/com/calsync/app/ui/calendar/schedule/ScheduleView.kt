package com.calsync.app.ui.calendar.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import java.util.*

@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.fillMaxSize()) {
        ScheduleHeader()
        ScheduleList()
    }
}

@Composable
fun ScheduleHeader() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 2.dp
    ) {
        Text(
            text = "今日日程",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun ScheduleList() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp),
                onClick = { }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("暂无日程", fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("点击右下角 + 添加新事件",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
