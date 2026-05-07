package com.calsync.app.ui.calendar.week

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeekScreen(
    viewModel: WeekViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val swipeThreshold = 100f

    Column(modifier = Modifier.fillMaxSize()) {
        WeekTopBar(
            weekStartDate = state.weekStartDate,
            days = state.days,
            onPreviousWeek = { viewModel.goToPreviousWeek() },
            onNextWeek = { viewModel.goToNextWeek() },
            onToday = { viewModel.goToToday() }
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    var totalDrag = 0f
                    detectHorizontalDragGestures(
                        onDragStart = { totalDrag = 0f },
                        onDragEnd = {
                            if (totalDrag > swipeThreshold) {
                                viewModel.goToPreviousWeek()
                            } else if (totalDrag < -swipeThreshold) {
                                viewModel.goToNextWeek()
                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            totalDrag += dragAmount
                        }
                    )
                }
        ) {
            WeekContent(days = state.days)
        }
    }
}

@Composable
fun WeekTopBar(
    weekStartDate: Long,
    days: List<WeekDay>,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit,
    onToday: () -> Unit
) {
    val cal = Calendar.getInstance()
    cal.timeInMillis = weekStartDate
    val weekStart = cal.get(Calendar.DAY_OF_MONTH)
    cal.add(Calendar.DAY_OF_YEAR, 6)
    val weekEnd = cal.get(Calendar.DAY_OF_MONTH)
    val monthSdf = SimpleDateFormat("M\u6708", Locale.getDefault())

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousWeek) {
            Icon(Icons.Default.ChevronLeft, contentDescription = "\u4e0a\u4e00\u5468")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${monthSdf.format(weekStartDate)}${weekStart}\u65e5-${weekEnd}\u65e5",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row {
            TextButton(onClick = onToday) {
                Text("\u4eca\u5929", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = onNextWeek) {
                Icon(Icons.Default.ChevronRight, contentDescription = "\u4e0b\u4e00\u5468")
            }
        }
    }

    // Day headers
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        days.forEach { day ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = day.dayName.removePrefix("\u5468"),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = if (day.isToday) MaterialTheme.colorScheme.primary
                    else Color.Transparent,
                    modifier = Modifier.size(28.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = day.dayOfMonth.toString(),
                            fontSize = 13.sp,
                            fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.Normal,
                            color = if (day.isToday) Color.White
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
fun WeekContent(days: List<WeekDay>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(24) { hour ->
            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = String.format("%02d:00", hour),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(45.dp)
                        .padding(end = 4.dp)
                        .align(Alignment.Top)
                )
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
