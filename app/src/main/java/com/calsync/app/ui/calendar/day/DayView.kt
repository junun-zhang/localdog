package com.calsync.app.ui.calendar.day

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
fun DayScreen(
    viewModel: DayViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.fillMaxSize()) {
        DayHeader()
        DayTimeline()
    }
}

@Composable
fun DayHeader() {
    val cal = Calendar.getInstance()
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

@Composable
fun DayTimeline() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(24) { hour ->
            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(60.dp)
            ) {
                // 时间标签
                Text(
                    text = String.format("%02d:00", hour),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(50.dp)
                        .padding(end = 8.dp)
                        .align(Alignment.Top)
                )

                // 时间槽
                Box(
                    modifier = Modifier.weight(1f)
                        .fillMaxHeight()
                        .padding(end = 8.dp)
                )
            }
        }
    }
}
