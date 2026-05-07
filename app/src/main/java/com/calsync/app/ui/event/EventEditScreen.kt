package com.calsync.app.ui.event

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventEditScreen(
    eventId: String? = null,
    onNavigateBack: () -> Unit,
    viewModel: EventViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var isAllDay by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableIntStateOf(0) }
    
    // Initialize with current date/time
    val now = System.currentTimeMillis()
    var startTime by remember { mutableLongStateOf(now) }
    var endTime by remember { mutableLongStateOf(now + 3600000) }

    // Picker states
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val timeSdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dateSdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

    // Date pickers
    if (showStartDatePicker) {
        val cal = Calendar.getInstance().apply { timeInMillis = startTime }
        DatePickerDialog(
            context,
            { _, year, month, day ->
                val newCal = Calendar.getInstance().apply {
                    timeInMillis = startTime
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, day)
                }
                startTime = newCal.timeInMillis
                showStartDatePicker = false
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    if (showEndDatePicker) {
        val cal = Calendar.getInstance().apply { timeInMillis = endTime }
        DatePickerDialog(
            context,
            { _, year, month, day ->
                val newCal = Calendar.getInstance().apply {
                    timeInMillis = endTime
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, day)
                }
                endTime = newCal.timeInMillis
                showEndDatePicker = false
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    // Time pickers
    if (showStartTimePicker) {
        val cal = Calendar.getInstance().apply { timeInMillis = startTime }
        TimePickerDialog(
            context,
            { _, hour, minute ->
                val newCal = Calendar.getInstance().apply {
                    timeInMillis = startTime
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                }
                startTime = newCal.timeInMillis
                showStartTimePicker = false
            },
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    if (showEndTimePicker) {
        val cal = Calendar.getInstance().apply { timeInMillis = endTime }
        TimePickerDialog(
            context,
            { _, hour, minute ->
                val newCal = Calendar.getInstance().apply {
                    timeInMillis = endTime
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                }
                endTime = newCal.timeInMillis
                showEndTimePicker = false
            },
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    val colors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.error,
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.secondary,
        Color(0xFF7B1FA2),
        Color(0xFFF9A825)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (eventId == null) "创建事件" else "编辑事件") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("\u2190", fontSize = 24.sp)
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            if (title.isNotBlank()) {
                                viewModel.createEvent(
                                    title = title,
                                    startTime = startTime,
                                    endTime = endTime,
                                    isAllDay = isAllDay,
                                    description = description.ifEmpty { null },
                                    location = location.ifEmpty { null },
                                    color = selectedColor
                                ) {
                                    onNavigateBack()
                                }
                            }
                        },
                        enabled = title.isNotBlank()
                    ) {
                        Text("保存", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("事件标题") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("输入事件标题") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("全天事件", modifier = Modifier.weight(1f), fontSize = 15.sp)
                Switch(checked = isAllDay, onCheckedChange = { isAllDay = it })
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            if (!isAllDay) {
                DateTimePickerRow(
                    label = "开始时间",
                    dateStr = dateSdf.format(startTime),
                    timeStr = timeSdf.format(startTime),
                    onDateClick = { showStartDatePicker = true },
                    onTimeClick = { showStartTimePicker = true }
                )
                Spacer(modifier = Modifier.height(12.dp))
                DateTimePickerRow(
                    label = "结束时间",
                    dateStr = dateSdf.format(endTime),
                    timeStr = timeSdf.format(endTime),
                    onDateClick = { showEndDatePicker = true },
                    onTimeClick = { showEndTimePicker = true }
                )
            } else {
                Text("全天事件", fontSize = 16.sp, modifier = Modifier.padding(vertical = 8.dp))
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("地点") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("输入地点") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("描述") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                placeholder = { Text("添加描述") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("颜色", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                colors.forEachIndexed { index, color ->
                    Surface(
                        shape = CircleShape,
                        color = color,
                        modifier = Modifier.size(32.dp)
                            .clickable { selectedColor = index }
                    ) {
                        if (selectedColor == index) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("\u2713", color = Color.White, fontSize = 16.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DateTimePickerRow(
    label: String,
    dateStr: String,
    timeStr: String,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onDateClick,
                modifier = Modifier.weight(1f)
            ) {
                Text(dateStr, fontSize = 15.sp)
            }
            OutlinedButton(
                onClick = onTimeClick,
                modifier = Modifier.weight(1f)
            ) {
                Text(timeStr, fontSize = 15.sp)
            }
        }
    }
}
