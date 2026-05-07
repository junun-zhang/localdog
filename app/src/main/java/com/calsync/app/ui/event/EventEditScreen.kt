package com.calsync.app.ui.event

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.hilt.navigation.compose.hiltViewModel
import com.calsync.app.domain.model.Event
import com.calsync.app.domain.util.RecurrenceRule
import java.text.SimpleDateFormat
import java.util.*

data class ReminderEntry(val minutesBefore: Int, val enabled: Boolean)

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
    
    // Recurrence rule
    var recurrenceFreq by remember { mutableIntStateOf(0) }
    var recurrenceExpanded by remember { mutableStateOf(false) }
    val recurrenceOptions = listOf("\u4e0d\u91cd\u590d", "\u6bcf\u5929", "\u6bcf\u5468", "\u6bcf\u6708", "\u6bcf\u5e74")
    
    // Multiple reminders
    var remindersList by remember { mutableStateOf<List<ReminderEntry>>(listOf(ReminderEntry(15, true))) }
    
    // Initialize with current date/time
    val now = System.currentTimeMillis()
    var startTime by remember { mutableLongStateOf(now) }
    var endTime by remember { mutableLongStateOf(now + 3600000) }

    // Picker states
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    
    // Loading state for edit mode
    var isLoadingEvent by remember { mutableStateOf(eventId != null) }
    var eventLoaded by remember { mutableStateOf(false) }
    val isEditing = eventId != null && eventLoaded

    val context = LocalContext.current
    val timeSdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dateSdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

    // Load existing event data in edit mode
    LaunchedEffect(eventId) {
        if (eventId != null) {
            isLoadingEvent = true
            viewModel.loadEventById(eventId) { result ->
                result.onSuccess { event ->
                    title = event.title
                    description = event.description ?: ""
                    location = event.location ?: ""
                    isAllDay = event.isAllDay
                    selectedColor = event.color
                    startTime = event.startTime
                    endTime = event.endTime
                    
                    recurrenceFreq = when (event.recurrenceRule?.freq) {
                        RecurrenceRule.Freq.DAILY -> 1
                        RecurrenceRule.Freq.WEEKLY -> 2
                        RecurrenceRule.Freq.MONTHLY -> 3
                        RecurrenceRule.Freq.YEARLY -> 4
                        null -> 0
                    }
                    
                    remindersList = if (event.reminders.isEmpty()) {
                        listOf(ReminderEntry(15, true))
                    } else {
                        event.reminders.map { ReminderEntry(it.minutesBefore, it.enabled) }
                    }
                    
                    eventLoaded = true
                    isLoadingEvent = false
                }.onFailure {
                    isLoadingEvent = false
                    eventLoaded = false
                }
            }
        }
    }

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

    if (isLoadingEvent) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "\u7f16\u8f91\u4e8b\u4ef6" else "\u521b\u5efa\u4e8b\u4ef6") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("\u2190", fontSize = 24.sp)
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            val recurrenceRule = if (recurrenceFreq > 0) {
                                    RecurrenceRule(
                                        freq = when (recurrenceFreq) {
                                            1 -> RecurrenceRule.Freq.DAILY
                                            2 -> RecurrenceRule.Freq.WEEKLY
                                            3 -> RecurrenceRule.Freq.MONTHLY
                                            else -> RecurrenceRule.Freq.YEARLY
                                        },
                                        interval = 1
                                    )
                                } else null
                                val reminderList = remindersList
                                    .filter { it.enabled }
                                    .map { Event.Reminder(minutesBefore = it.minutesBefore, enabled = true) }
                                
                                if (isEditing) {
                                    viewModel.loadEventById(eventId!!) { result ->
                                        result.onSuccess { existing ->
                                            val updated = existing.copy(
                                                title = title.ifBlank { "\u65b0\u4e8b\u4ef6" },
                                                startTime = startTime,
                                                endTime = endTime,
                                                isAllDay = isAllDay,
                                                description = description.ifEmpty { null },
                                                location = location.ifEmpty { null },
                                                color = selectedColor,
                                                recurrenceRule = recurrenceRule,
                                                reminders = reminderList
                                            )
                                            viewModel.updateEvent(updated) {
                                                onNavigateBack()
                                            }
                                        }.onFailure {
                                            onNavigateBack()
                                        }
                                    }
                                } else {
                                    viewModel.createEvent(
                                        title = title.ifBlank { "\u65b0\u4e8b\u4ef6" },
                                        startTime = startTime,
                                        endTime = endTime,
                                        isAllDay = isAllDay,
                                        description = description.ifEmpty { null },
                                        location = location.ifEmpty { null },
                                        color = selectedColor,
                                        recurrenceRule = recurrenceRule,
                                        reminders = reminderList
                                    ) {
                                        onNavigateBack()
                                    }
                                }
                        },
                        enabled = true
                    ) {
                        Text("\u4fdd\u5b58", fontWeight = FontWeight.Bold)
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
                label = { Text("\u4e8b\u4ef6\u6807\u9898") },
                modifier = Modifier.fillMaxWidth().semantics {
                    contentDescription = "\u4e8b\u4ef6\u6807\u9898\u8f93\u5165"
                },
                placeholder = { Text("\u8f93\u5165\u4e8b\u4ef6\u6807\u9898") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("\u5168\u5929\u4e8b\u4ef6", modifier = Modifier.weight(1f), fontSize = 15.sp)
                Switch(checked = isAllDay, onCheckedChange = { isAllDay = it })
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            if (!isAllDay) {
                DateTimePickerRow(
                    label = "\u5f00\u59cb\u65f6\u95f4",
                    dateStr = dateSdf.format(startTime),
                    timeStr = timeSdf.format(startTime),
                    onDateClick = { showStartDatePicker = true },
                    onTimeClick = { showStartTimePicker = true }
                )
                Spacer(modifier = Modifier.height(12.dp))
                DateTimePickerRow(
                    label = "\u7ed3\u675f\u65f6\u95f4",
                    dateStr = dateSdf.format(endTime),
                    timeStr = timeSdf.format(endTime),
                    onDateClick = { showEndDatePicker = true },
                    onTimeClick = { showEndTimePicker = true }
                )
            } else {
                Text("\u5168\u5929\u4e8b\u4ef6", fontSize = 16.sp, modifier = Modifier.padding(vertical = 8.dp))
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("\u5730\u70b9") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("\u8f93\u5165\u5730\u70b9") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("\u63cf\u8ff0") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                placeholder = { Text("\u6dfb\u52a0\u63cf\u8ff0") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("\u989c\u8272", fontSize = 16.sp, fontWeight = FontWeight.Medium)
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

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text("\u91cd\u590d", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            ExposedDropdownMenuBox(
                expanded = recurrenceExpanded,
                onExpandedChange = { recurrenceExpanded = it }
            ) {
                OutlinedTextField(
                    value = recurrenceOptions[recurrenceFreq],
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = recurrenceExpanded) }
                )
                ExposedDropdownMenu(
                    expanded = recurrenceExpanded,
                    onDismissRequest = { recurrenceExpanded = false }
                ) {
                    recurrenceOptions.forEachIndexed { index, option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                recurrenceFreq = index
                                recurrenceExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("\u63d0\u9192", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            
            remindersList.forEachIndexed { index, reminder ->
                ReminderRow(
                    reminder = reminder,
                    onMinutesChange = { newMinutes ->
                        val mutable = remindersList.toMutableList()
                        mutable[index] = mutable[index].copy(minutesBefore = newMinutes)
                        remindersList = mutable
                    },
                    onDelete = {
                        val mutable = remindersList.toMutableList()
                        mutable.removeAt(index)
                        remindersList = if (mutable.isEmpty()) {
                            listOf(ReminderEntry(0, false))
                        } else mutable
                    },
                    showDelete = remindersList.size > 1 || (remindersList.size == 1 && remindersList[0].minutesBefore > 0)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            TextButton(
                onClick = {
                    remindersList = remindersList + ReminderEntry(15, true)
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("\u6dfb\u52a0\u63d0\u9192")
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderRow(
    reminder: ReminderEntry,
    onMinutesChange: (Int) -> Unit,
    onDelete: () -> Unit,
    showDelete: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    val reminderOptions = listOf(
        0 to "\u4e0d\u63d0\u9192",
        5 to "5\u5206\u949f\u524d",
        10 to "10\u5206\u949f\u524d",
        15 to "15\u5206\u949f\u524d",
        30 to "30\u5206\u949f\u524d",
        60 to "1\u5c0f\u65f6\u524d"
    )
    val selectedLabel = reminderOptions.first { it.first == reminder.minutesBefore }.second

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.weight(1f)
        ) {
            OutlinedTextField(
                value = selectedLabel,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                reminderOptions.forEach { (minutes, label) ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            onMinutesChange(minutes)
                            expanded = false
                        }
                    )
                }
            }
        }

        if (showDelete) {
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "\u5220\u9664\u63d0\u9192",
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.error
                )
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
