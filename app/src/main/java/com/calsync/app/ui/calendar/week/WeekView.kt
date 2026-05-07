package com.calsync.app.ui.calendar.week

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.calsync.app.domain.model.Event
import com.calsync.app.domain.util.WeatherProvider
import com.calsync.app.ui.common.WeatherCompact
import java.text.SimpleDateFormat
import java.util.*

private val HOUR_HEIGHT = 50.dp
private val TIMELINE_WIDTH = 45.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeekScreen(
    onDayClick: ((Long) -> Unit)? = null,
    onEventClick: ((String) -> Unit)? = null,
    viewModel: WeekViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val swipeThreshold = 100f

    val weekWeather = remember(state.weekStartDate) {
        WeatherProvider.getWeekWeather(state.weekStartDate)
    }

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
            WeekContent(
                days = state.days,
                eventsByDay = state.events,
                onEventClick = onEventClick,
                onDayClick = onDayClick
            )
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
                text = "${monthSdf.format(weekStartDate)}$weekStart\u65e5-$weekEnd\u65e5",
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
    HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))
}

@Composable
fun WeekContent(
    days: List<WeekDay>,
    eventsByDay: Map<Long, List<Event>> = emptyMap(),
    onEventClick: ((String) -> Unit)? = null,
    onDayClick: ((Long) -> Unit)? = null
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val totalHeight = 24 * HOUR_HEIGHT.value
        val totalWidth = this.maxWidth

        // Background grid with hour labels
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(24) { hour ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .height(HOUR_HEIGHT)
                        .padding(end = 8.dp)
                ) {
                    // Hour label
                    Text(
                        text = String.format("%02d:00", hour),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.width(TIMELINE_WIDTH)
                            .padding(end = 4.dp)
                            .align(Alignment.Top)
                    )
                    // 7 day columns background
                    repeat(7) { dayIdx ->
                        Box(
                            modifier = Modifier.fillMaxHeight()
                                .weight(1f)
                                .padding(start = 1.dp, end = 1.dp)
                                .background(
                                    color = if (days.getOrNull(dayIdx)?.isToday == true)
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.04f)
                                    else Color.Transparent
                                )
                        )
                    }
                }
                if (hour < 23) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    )
                }
            }
        }

        // Overlay events on top of grid
        if (eventsByDay.isNotEmpty()) {
            val columnWidth = (totalWidth - TIMELINE_WIDTH) / 7f
            days.forEachIndexed { dayIdx, day ->
                val dayEvents = eventsByDay[day.timestamp] ?: emptyList()
                dayEvents.forEach { event ->
                    val startMin = getMinutesFromMidnight(event.startTime)
                    val durationMs = event.endTime - event.startTime
                    val durationMin = (durationMs / (1000 * 60)).coerceIn(30, 1440)

                    val topPx = (startMin.toFloat() / 60f) * HOUR_HEIGHT.value
                    val heightPx = (durationMin.toFloat() / 60f) * HOUR_HEIGHT.value

                    val topOffset = topPx.coerceIn(0f, totalHeight).dp
                    val eventHeight = heightPx.coerceIn(16f, HOUR_HEIGHT.value * 6).dp
                    val leftOffset = (TIMELINE_WIDTH.value + dayIdx * columnWidth.value + 2).dp

                    if (topOffset.value < totalHeight) {
                        Box(
                            modifier = Modifier
                                .offset(x = leftOffset, y = topOffset)
                                .width((columnWidth.value - 4).dp)
                                .height(eventHeight)
                                .clickable { onEventClick?.invoke(event.id) }
                                .semantics {
                                    contentDescription = "周视图事件: " + event.title
                                }
                                .background(
                                    color = event.getEventColor().copy(alpha = 0.2f),
                                    shape = MaterialTheme.shapes.extraSmall
                                )
                                .padding(horizontal = 2.dp, vertical = 1.dp),
                            contentAlignment = Alignment.TopStart
                        ) {
                            Column {
                                Text(
                                    text = event.title,
                                    fontSize = if (eventHeight.value < 24) 9.sp else 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = event.getEventColor(),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                if (eventHeight.value >= 36 && !event.isAllDay) {
                                    val timeSdf = SimpleDateFormat("HH:mm", Locale.getDefault())
                                    Text(
                                        text = timeSdf.format(event.startTime),
                                        fontSize = 8.sp,
                                        color = event.getEventColor().copy(alpha = 0.7f),
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getMinutesFromMidnight(timestamp: Long): Int {
    val cal = Calendar.getInstance()
    cal.timeInMillis = timestamp
    return cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
}
