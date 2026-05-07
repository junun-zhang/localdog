package com.calsync.app.ui.event

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.calsync.app.ui.common.formatEventTime
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: String,
    onNavigateBack: () -> Unit,
    onEdit: () -> Unit,
    viewModel: EventViewModel = hiltViewModel()
) {
    var event by remember { mutableStateOf<com.calsync.app.domain.model.Event?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(eventId) {
        viewModel.loadEventById(eventId) { result ->
            result.onSuccess { loadedEvent ->
                event = loadedEvent
                isLoading = false
            }.onFailure { e ->
                error = e.message
                isLoading = false
            }
        }
    }

    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        error != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("加载失败: $error", color = MaterialTheme.colorScheme.error)
            }
        }
        event == null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("事件未找到", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        else -> {
            val currentEvent = event!!
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("事件详情") },
                        navigationIcon = {
                            IconButton(onClick = onNavigateBack) {
                                Text("\u2190", fontSize = 24.sp)
                            }
                        },
                        actions = {
                            IconButton(onClick = onEdit) {
                                Icon(Icons.Default.Edit, contentDescription = "编辑")
                            }
                            IconButton(onClick = {
                                viewModel.deleteEvent(currentEvent) { onNavigateBack() }
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "删除", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    )
                }
            ) { padding ->
                Column(
                    modifier = Modifier.fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                ) {
                    // 顶部颜色条
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .height(4.dp)
                            .background(currentEvent.getEventColor())
                    )

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(currentEvent.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)

                        Spacer(modifier = Modifier.height(16.dp))

                        // 时间
                        DetailRow(
                            icon = Icons.Default.Schedule,
                            label = formatEventTime(currentEvent)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // 地点
                        if (currentEvent.location != null) {
                            DetailRow(
                                icon = Icons.Default.LocationOn,
                                label = currentEvent.location
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // 重复规则
                        if (currentEvent.recurrenceRule != null) {
                            DetailRow(
                                icon = Icons.Default.Repeat,
                                label = getRecurrenceDescription(currentEvent.recurrenceRule)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // 提醒
                        if (currentEvent.reminders.isNotEmpty()) {
                            DetailRow(
                                icon = Icons.Default.Notifications,
                                label = getRemindersDescription(currentEvent.reminders)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // 描述
                        if (currentEvent.description != null) {
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            Text("描述", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(currentEvent.description, fontSize = 15.sp)
                        }
                    }
                }
            }
        }
    }
}

private fun getRecurrenceDescription(rule: com.calsync.app.domain.util.RecurrenceRule): String {
    return when (rule.freq) {
        com.calsync.app.domain.util.RecurrenceRule.Freq.DAILY -> "每天重复"
        com.calsync.app.domain.util.RecurrenceRule.Freq.WEEKLY -> "每周重复"
        com.calsync.app.domain.util.RecurrenceRule.Freq.MONTHLY -> "每月重复"
        com.calsync.app.domain.util.RecurrenceRule.Freq.YEARLY -> "每年重复"
    }
}

private fun getRemindersDescription(reminders: List<com.calsync.app.domain.model.Event.Reminder>): String {
    if (reminders.isEmpty()) return "无提醒"
    return reminders.filter { it.enabled }.joinToString(", ") {
        when (it.minutesBefore) {
            0 -> "事件开始时"
            5 -> "提前5分钟"
            10 -> "提前10分钟"
            15 -> "提前15分钟"
            30 -> "提前30分钟"
            60 -> "提前1小时"
            else -> "提前${it.minutesBefore}分钟"
        }
    }
}

@Composable
fun DetailRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, fontSize = 15.sp)
    }
}
