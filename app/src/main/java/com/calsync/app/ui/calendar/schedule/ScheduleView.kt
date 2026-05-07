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
import com.calsync.app.ui.common.EventCard
import com.calsync.app.ui.event.EventViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ScheduleScreen(
    viewModel: EventViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Load today's events
    LaunchedEffect(Unit) {
        viewModel.loadEventsForDate(System.currentTimeMillis())
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ScheduleHeader()
        ScheduleList(events = state.events)
    }
}

@Composable
fun ScheduleHeader() {
    val cal = Calendar.getInstance()
    val dateSdf = SimpleDateFormat("M月d日", Locale.getDefault())

    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "今日日程",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = dateSdf.format(System.currentTimeMillis()),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ScheduleList(events: List<com.calsync.app.domain.model.Event>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        if (events.isEmpty()) {
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
        } else {
            items(events.size) { i ->
                EventCard(
                    event = events[i],
                    onClick = { /* TODO: 导航到详情 */ },
                    showActions = true,
                    onEdit = { /* TODO: 导航到编辑 */ },
                    onDelete = { /* TODO: 删除事件 */ }
                )
            }
        }
    }
}
