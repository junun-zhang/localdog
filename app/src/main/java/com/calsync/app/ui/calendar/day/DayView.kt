package com.calsync.app.ui.calendar.day

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.calsync.app.domain.model.Weather
import com.calsync.app.domain.util.WeatherProvider
import com.calsync.app.ui.common.WeatherCompact
import com.calsync.app.domain.model.Event
import com.calsync.app.ui.common.EventCardCompact
import com.calsync.app.ui.event.EventViewModel
import java.text.SimpleDateFormat
import java.util.*

private val HOUR_HEIGHT = 60.dp
private val TIMELINE_WIDTH = 50.dp
private val MAX_EVENT_HEIGHT = 1200.dp

@Composable
fun DayScreen(
    selectedDate: Long = System.currentTimeMillis(),
    onEventClick: ((String) -> Unit)? = null,
    dayViewModel: DayViewModel = hiltViewModel(),
    eventViewModel: EventViewModel = hiltViewModel()
) {
    val dayState by dayViewModel.state.collectAsState()
    val eventState by eventViewModel.state.collectAsState()
    val events = eventState.events
    val swipeThreshold = 100f

    // Sync initial selectedDate to DayViewModel
    LaunchedEffect(selectedDate) {
        dayViewModel.setDate(selectedDate)
    }

    // Load events when date changes
    LaunchedEffect(dayState.selectedDate) {
        eventViewModel.loadEventsForDate(dayState.selectedDate)
    }

    val weather = remember(dayState.selectedDate) {
        WeatherProvider.getWeatherForDate(dayState.selectedDate)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        DayHeader(
            selectedDate = dayState.selectedDate,
            onPreviousDay = { dayViewModel.goToPreviousDay() },
            onNextDay = { dayViewModel.goToNextDay() },
            onToday = { dayViewModel.goToToday() },
            weather = weather
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
                                dayViewModel.goToPreviousDay()
                            } else if (totalDrag < -swipeThreshold) {
                                dayViewModel.goToNextDay()
                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            totalDrag += dragAmount
                        }
                    )
                }
        ) {
            if (events.isEmpty()) {
                EmptyTimeline()
            } else {
                TimelineWithEvents(events, onEventClick)
            }
        }
    }
}

@Composable
fun DayHeader(
    selectedDate: Long,
    onPreviousDay: () -> Unit,
    onNextDay: () -> Unit,
    onToday: () -> Unit,
    weather: com.calsync.app.domain.model.Weather? = null
) {
    val cal = Calendar.getInstance()
    cal.timeInMillis = selectedDate
    val dayNames = listOf("周日", "周一", "周二", "周三", "周四", "周五", "周六")
    val dayName = dayNames[cal.get(Calendar.DAY_OF_WEEK) - 1]
    val dateSdf = SimpleDateFormat("M月d日", Locale.getDefault())

    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPreviousDay) {
                Icon(Icons.Default.ChevronLeft, contentDescription = "前一天")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = dateSdf.format(selectedDate),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = dayName,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                weather?.let { WeatherCompact(weather = it) }
                TextButton(onClick = onToday) {
                    Text("今天", fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onNextDay) {
                    Icon(Icons.Default.ChevronRight, contentDescription = "下一天")
                }
            }
        }
    }
}

@Composable
fun EmptyTimeline() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(24) { hour ->
            HourRow(hour)
        }
    }
}

@Composable
fun TimelineWithEvents(events: List<Event>, onEventClick: ((String) -> Unit)? = null) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val totalHeight = 24 * HOUR_HEIGHT.value
        val maxH = this.maxHeight.value

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(24) { hour ->
                HourRow(hour)
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            events.forEach { event ->
                val startMin = getMinutesFromMidnight(event.startTime)
                val durationMs = event.endTime - event.startTime
                val durationMin = (durationMs / (1000 * 60)).coerceIn(0, 1440)

                val topPx = (startMin.toFloat() / 60f) * HOUR_HEIGHT.value
                val heightPx = (durationMin.toFloat() / 60f) * HOUR_HEIGHT.value

                val topOffset = topPx.coerceIn(0f, totalHeight).dp
                val eventHeight = heightPx.coerceIn(20f, MAX_EVENT_HEIGHT.value).dp

                if (topOffset.value < totalHeight) {
                    Box(
                        modifier = Modifier
                            .padding(start = TIMELINE_WIDTH + 8.dp, end = 8.dp)
                            .offset(y = topOffset)
                            .height(eventHeight)
                            .fillMaxWidth()
                    ) {
                        EventCardCompact(
                            event = event,
                            onClick = { onEventClick?.invoke(event.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HourRow(hour: Int) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .height(HOUR_HEIGHT)
    ) {
        Text(
            text = String.format("%02d:00", hour),
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(TIMELINE_WIDTH)
                .padding(end = 8.dp)
                .align(Alignment.Top)
        )
        Box(
            modifier = Modifier.weight(1f)
                .fillMaxHeight()
                .padding(end = 8.dp)
                .background(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f)
                )
        )
    }
}

private fun getMinutesFromMidnight(timestamp: Long): Int {
    val cal = Calendar.getInstance()
    cal.timeInMillis = timestamp
    return cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
}
