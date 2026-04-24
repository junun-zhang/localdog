package com.calsync.app.ui.event

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
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
    var startTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var endTime by remember { mutableLongStateOf(System.currentTimeMillis() + 3600000) }

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
                    TextButton(onClick = {
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
                    }) {
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
                Text("全天事件", modifier = Modifier.weight(1f))
                Switch(checked = isAllDay, onCheckedChange = { isAllDay = it })
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            if (!isAllDay) {
                TimePickerRow(label = "开始时间", timestamp = startTime, onTimeChange = { startTime = it })
                Spacer(modifier = Modifier.height(8.dp))
                TimePickerRow(label = "结束时间", timestamp = endTime, onTimeChange = { endTime = it })
            } else {
                Text("全天事件", fontSize = 16.sp)
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
fun TimePickerRow(label: String, timestamp: Long, onTimeChange: (Long) -> Unit) {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable { }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 15.sp)
        Text(sdf.format(timestamp), fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
    }
}
