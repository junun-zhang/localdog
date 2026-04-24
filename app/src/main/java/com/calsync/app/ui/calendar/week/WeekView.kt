package com.calsync.app.ui.calendar.week

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
fun WeekScreen(
    viewModel: WeekViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.fillMaxSize()) {
        WeekHeader()
        WeekGrid()
    }
}

@Composable
fun WeekHeader() {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        repeat(7) { i ->
            val dayNames = listOf("日", "一", "二", "三", "四", "五", "六")
            val cal = java.util.Calendar.getInstance()
            cal.add(java.util.Calendar.DAY_OF_WEEK, i)
            val day = cal.get(java.util.Calendar.DAY_OF_MONTH)
            val isToday = cal.get(java.util.Calendar.DAY_OF_YEAR) ==
                    java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR)

            Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                Text(
                    text = dayNames[cal.get(java.util.Calendar.DAY_OF_WEEK) - 1],
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = if (isToday) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surface,
                    modifier = Modifier.size(28.dp)
                ) {
                    Box(contentAlignment = androidx.compose.ui.Alignment.Center) {
                        Text(
                            text = "$day",
                            fontSize = 13.sp,
                            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                            color = if (isToday) androidx.compose.ui.graphics.Color.White
                            else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
    Divider(modifier = Modifier.padding(horizontal = 8.dp))
}

@Composable
fun WeekGrid() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(24) { hour ->
            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(7) {
                    Box(
                        modifier = Modifier.fillMaxHeight()
                            .weight(1f)
                            .padding(1.dp)
                    )
                }
            }
            if (hour < 23) {
                Divider(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )
            }
        }
    }
}
