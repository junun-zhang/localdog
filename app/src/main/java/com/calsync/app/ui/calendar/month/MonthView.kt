package com.calsync.app.ui.calendar.month

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.calsync.app.ui.theme.EventBlue

@Composable
fun MonthScreen(
    viewModel: MonthViewModel = hiltViewModel(),
    onNavigateToDay: ((Long) -> Unit)? = null
) {
    val state by viewModel.state.collectAsState()
    val swipeThreshold = 100f

    Column(modifier = Modifier.fillMaxSize()) {
        MonthHeader(
            year = state.currentYear,
            month = state.currentMonth,
            onPreviousMonth = { viewModel.goToPreviousMonth() },
            onNextMonth = { viewModel.goToNextMonth() },
            onToday = { viewModel.goToToday() }
        )
        WeekHeader()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    var totalDrag = 0f
                    detectHorizontalDragGestures(
                        onDragStart = { totalDrag = 0f },
                        onDragEnd = {
                            if (totalDrag > swipeThreshold) {
                                viewModel.goToPreviousMonth()
                            } else if (totalDrag < -swipeThreshold) {
                                viewModel.goToNextMonth()
                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            totalDrag += dragAmount
                        }
                    )
                }
        ) {
            CalendarGrid(
                days = state.days,
                selectedDate = state.selectedDate,
                onDayClick = { ts ->
                    viewModel.selectDate(ts)
                    onNavigateToDay?.invoke(ts)
                }
            )
        }
    }
}

@Composable
fun MonthHeader(
    year: Int,
    month: Int,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onToday: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousMonth) {
            Text("\u2039", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = year.toString() + "\u5e74" + month.toString() + "\u6708",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            val lm = listOf("", "\u6b63\u6708", "\u4e8c\u6708", "\u4e09\u6708", "\u56db\u6708", "\u4e94\u6708", "\u516d\u6708", "\u4e03\u6708", "\u516b\u6708", "\u4e5d\u6708", "\u5341\u6708", "\u5341\u4e00\u6708", "\u5341\u4e8c\u6708")
            Text(
                text = "\u519c\u5386" + lm[month],
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Row {
            IconButton(onClick = onToday) {
                Text("\u4eca", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = onNextMonth) {
                Text("\u203A", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun WeekHeader() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        listOf("\u65e5", "\u4e00", "\u4e8c", "\u4e09", "\u56db", "\u4e94", "\u516d").forEach {
            Text(
                text = it,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
    Divider(modifier = Modifier.padding(horizontal = 8.dp))
}

@Composable
fun CalendarGrid(days: List<CalendarDay>, selectedDate: Long?, onDayClick: (Long) -> Unit) {
    LazyVerticalGrid(columns = GridCells.Fixed(7), modifier = Modifier.fillMaxSize()) {
        items(days.size) { i ->
            val day = days[i]
            Box(
                modifier = Modifier.aspectRatio(1f).padding(2.dp).clickable { onDayClick(day.timestamp) },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(32.dp)) {
                        if (day.isToday) {
                            Surface(
                                shape = MaterialTheme.shapes.small,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(30.dp)
                            ) {}
                            Text(text = day.dayOfMonth.toString(), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        } else {
                            val tc = if (!day.isCurrentMonth) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f) else MaterialTheme.colorScheme.onSurface
                            Text(text = day.dayOfMonth.toString(), fontSize = 14.sp, color = tc)
                        }
                    }
                    val sub = when {
                        day.solarTerm != null -> day.solarTerm
                        day.holidayName != null -> day.holidayName
                        day.lunarDay != "\u521d\u4e00" -> day.lunarDay
                        else -> day.lunarMonth
                    }
                    if (sub.isNotEmpty() && day.isCurrentMonth) {
                        Text(
                            text = sub,
                            fontSize = 9.sp,
                            color = if (day.solarTerm != null || day.holidayName != null) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            maxLines = 1
                        )
                    }
                    if (day.hasEvents) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Surface(shape = CircleShape, color = EventBlue, modifier = Modifier.size(4.dp)) {}
                    }
                }
            }
        }
    }
}
