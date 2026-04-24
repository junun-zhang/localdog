package com.calsync.app.ui.calendar.day

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.calsync.app.ui.event.EventViewModel
import com.calsync.app.ui.common.EventCard
import java.util.*

@Composable
fun DayScreen(
    selectedDate: Long = System.currentTimeMillis(),
    viewModel: EventViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(selectedDate) {
        viewModel.loadEventsForDate(selectedDate)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        DayHeader(selectedDate = selectedDate)
        if (state.events.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("今日暂无事件", fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.events.size) { i ->
                    EventCard(
                        event = state.events[i],
                        onClick = { /* TODO: 导航到详情 */ }
                    )
                }
            }
        }
    }
}

@Composable
fun DayHeader(selectedDate: Long) {
    val cal = Calendar.getInstance()
    cal.timeInMillis = selectedDate
    val dayNames = listOf("周日", "周一", "周二", "周三", "周四", "周五", "周六")
    val dayName = dayNames[cal.get(Calendar.DAY_OF_WEEK) - 1]

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
                    text = "${cal.get(Calendar.MONTH) + 1}月${cal.get(Calendar.DAY_OF_MONTH)}日",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = dayName,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}