package com.calsync.app.ui.calendar.month

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.calsync.app.ui.common.WeatherIcon
import com.calsync.app.ui.theme.EventBlue

@Composable
fun MonthScreen(
    viewModel: MonthViewModel = hiltViewModel(),
    onNavigateToDay: ((Long) -> Unit)? = null,
    onSearchClick: (() -> Unit)? = null
) {
    val state by viewModel.state.collectAsState()
    val swipeThreshold = 100f

    var showYearMonthPicker by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        MonthHeader(
            year = state.currentYear,
            month = state.currentMonth,
            onPreviousMonth = { viewModel.goToPreviousMonth() },
            onNextMonth = { viewModel.goToNextMonth() },
            onToday = { viewModel.goToToday() },
            onSearchClick = onSearchClick,
            onYearMonthClick = { showYearMonthPicker = true },
            onSettingsClick = { showSettings = true }
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
                showWeather = state.settings.showWeather,
                temperatureUnit = state.settings.temperatureUnit,
                onDayClick = { ts ->
                    viewModel.selectDate(ts)
                    onNavigateToDay?.invoke(ts)
                }
            )
        }
    }

    // Year/Month Picker Dialog
    if (showYearMonthPicker) {
        YearMonthPickerDialog(
            currentYear = state.currentYear,
            currentMonth = state.currentMonth,
            onDismiss = { showYearMonthPicker = false },
            onConfirm = { year, month ->
                viewModel.goToYearMonth(year, month)
                showYearMonthPicker = false
            }
        )
    }

    // Settings Dialog
    if (showSettings) {
        MonthSettingsDialog(
            settings = state.settings,
            onDismiss = { showSettings = false },
            onHolidayRegionChange = { viewModel.updateHolidayRegion(it) },
            onWeatherToggle = { viewModel.toggleWeather(it) },
            onTemperatureUnitChange = { viewModel.updateTemperatureUnit(it) }
        )
    }
}

// ─── Header ─────────────────────────────────────────────

@Composable
fun MonthHeader(
    year: Int,
    month: Int,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onToday: () -> Unit,
    onSearchClick: (() -> Unit)? = null,
    onYearMonthClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 4.dp, top = 12.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onPreviousMonth) {
                Text("\u2039", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }
            // Clickable year/month area
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onYearMonthClick() }.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
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
        }
        Row {
            IconButton(onClick = onToday) {
                Text("\u4eca", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = onSettingsClick) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "\u6708\u5386\u8bbe\u7f6e",
                    modifier = Modifier.size(22.dp)
                )
            }
            IconButton(onClick = onNextMonth) {
                Text("\u203A", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ─── Week Header ─────────────────────────────────────────

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
    HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))
}

// ─── Calendar Grid ───────────────────────────────────────

@Composable
fun CalendarGrid(
    days: List<CalendarDay>,
    selectedDate: Long?,
    showWeather: Boolean = false,
    temperatureUnit: String = "C",
    onDayClick: (Long) -> Unit
) {
    LazyVerticalGrid(columns = GridCells.Fixed(7), modifier = Modifier.fillMaxSize()) {
        items(days.size) { i ->
            val day = days[i]
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(2.dp)
                    .clickable { onDayClick(day.timestamp) },
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Day number
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(28.dp)
                    ) {
                        if (day.isToday) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(26.dp)
                            ) {}
                            Text(
                                text = day.dayOfMonth.toString(),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        } else {
                            val tc = if (!day.isCurrentMonth)
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                            else MaterialTheme.colorScheme.onSurface
                            Text(
                                text = day.dayOfMonth.toString(),
                                fontSize = 13.sp,
                                color = tc
                            )
                        }
                    }

                    // Subtitle (lunar/holiday)
                    val sub = when {
                        day.solarTerm != null -> day.solarTerm
                        day.holidayName != null -> day.holidayName
                        day.lunarDay != "\u521d\u4e00" -> day.lunarDay
                        else -> day.lunarMonth
                    }
                    if (sub.isNotEmpty() && day.isCurrentMonth) {
                        Text(
                            text = sub,
                            fontSize = 8.sp,
                            color = if (day.solarTerm != null || day.holidayName != null)
                                MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            maxLines = 1
                        )
                    }

                    // Event dot
                    if (day.hasEvents) {
                        Spacer(modifier = Modifier.height(1.dp))
                        Surface(
                            shape = CircleShape,
                            color = EventBlue,
                            modifier = Modifier.size(4.dp)
                        ) {}
                    }

                    // Weather icon + temp (like Google Calendar - small at bottom of cell)
                    if (showWeather && day.isCurrentMonth && day.weather != null) {
                        Spacer(modifier = Modifier.height(1.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            WeatherIcon(
                                condition = day.weather.condition,
                                size = 10.dp
                            )
                            Spacer(modifier = Modifier.width(1.dp))
                            Text(
                                text = "${day.weather.temperature}\u00B0",
                                fontSize = 8.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─── Year/Month Picker Dialog ────────────────────────────

@Composable
fun YearMonthPickerDialog(
    currentYear: Int,
    currentMonth: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    var selectedYear by remember { mutableStateOf(currentYear) }
    var selectedMonth by remember { mutableStateOf(currentMonth) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 6.dp,
            modifier = Modifier.width(300.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = "\u9009\u62e9\u65e5\u671f",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Year navigator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { selectedYear-- }) {
                        Text("\u2039", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = selectedYear.toString() + "\u5e74",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = { selectedYear++ }) {
                        Text("\u203A", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                // Month grid
                val months = listOf(
                    "\u4e00\u6708", "\u4e8c\u6708", "\u4e09\u6708", "\u56db\u6708",
                    "\u4e94\u6708", "\u516d\u6708", "\u4e03\u6708", "\u516b\u6708",
                    "\u4e5d\u6708", "\u5341\u6708", "\u5341\u4e00\u6708", "\u5341\u4e8c\u6708"
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.height(200.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(12) { idx ->
                        val month = idx + 1
                        val isCurrent = month == currentMonth && selectedYear == currentYear
                        val isSelected = month == selectedMonth

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when {
                                isSelected -> MaterialTheme.colorScheme.primary
                                isCurrent -> MaterialTheme.colorScheme.primaryContainer
                                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedMonth = month }
                        ) {
                            Text(
                                text = months[idx],
                                modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected || isCurrent) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Buttons
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("\u53d6\u6d88")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { onConfirm(selectedYear, selectedMonth) }) {
                        Text("\u786e\u5b9a")
                    }
                }
            }
        }
    }
}

// ─── Settings Dialog ─────────────────────────────────────

@Composable
fun MonthSettingsDialog(
    settings: MonthViewSettings,
    onDismiss: () -> Unit,
    onHolidayRegionChange: (String) -> Unit,
    onWeatherToggle: (Boolean) -> Unit,
    onTemperatureUnitChange: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 6.dp,
            modifier = Modifier.width(320.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "\u6708\u5386\u8bbe\u7f6e",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(20.dp))

                // Holiday region selector
                Text(
                    text = "\u8282\u5047\u65e5\u5730\u533a",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))

                val regions = listOf("china", "usa", "japan")
                val regionLabels = mapOf("china" to "\u4e2d\u56fd", "usa" to "\u7f8e\u56fd", "japan" to "\u65e5\u672c")
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    regions.forEach { region ->
                        FilterChip(
                            selected = settings.holidayRegion == region,
                            onClick = { onHolidayRegionChange(region) },
                            label = {
                                Text(
                                    text = regionLabels[region] ?: region,
                                    fontSize = 13.sp
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Weather toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "\u5929\u6c14\u663e\u793a",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Switch(
                        checked = settings.showWeather,
                        onCheckedChange = onWeatherToggle
                    )
                }

                // Temperature unit (only visible when weather is on)
                if (settings.showWeather) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "\u6e29\u5ea6\u5355\u4f4d",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = settings.temperatureUnit == "C",
                            onClick = { onTemperatureUnitChange("C") },
                            label = { Text("\u00b0C") }
                        )
                        FilterChip(
                            selected = settings.temperatureUnit == "F",
                            onClick = { onTemperatureUnitChange("F") },
                            label = { Text("\u00b0F") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Close button
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = onDismiss) {
                        Text("\u5173\u95ed")
                    }
                }
            }
        }
    }
}
