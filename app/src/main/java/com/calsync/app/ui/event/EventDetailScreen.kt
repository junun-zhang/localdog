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
    // TODO: 从Repository加载事件
    val dummyEvent = remember {
        com.calsync.app.domain.model.Event(
            id = eventId,
            calendarId = "default",
            title = "示例事件",
            startTime = System.currentTimeMillis(),
            endTime = System.currentTimeMillis() + 3600000,
            description = "这是一个示例事件描述",
            location = "示例地点",
            color = 0
        )
    }

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
                        viewModel.deleteEvent(dummyEvent) { onNavigateBack() }
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
                    .background(dummyEvent.getEventColor())
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(dummyEvent.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(16.dp))

                // 时间
                DetailRow(
                    icon = Icons.Default.Schedule,
                    label = formatEventTime(dummyEvent)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 地点
                if (dummyEvent.location != null) {
                    DetailRow(
                        icon = Icons.Default.LocationOn,
                        label = dummyEvent.location
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // 描述
                if (dummyEvent.description != null) {
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("描述", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(dummyEvent.description, fontSize = 15.sp)
                }
            }
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
