package com.calsync.app.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.calsync.app.domain.model.Event
import com.calsync.app.domain.model.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onNavigateBack: () -> Unit,
    onEventClick: (String) -> Unit,
    onTaskClick: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val dateFormat = remember { SimpleDateFormat("MM/dd HH:mm", Locale.getDefault()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = state.query,
                        onValueChange = { viewModel.onQueryChanged(it) },
                        placeholder = { Text("搜索事件或待办...") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    if (state.query.isNotBlank()) {
                        IconButton(onClick = { viewModel.clearSearch() }) {
                            Icon(Icons.Default.Close, contentDescription = "清除")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            if (!state.hasSearched && state.query.isBlank()) {
                // Empty state - show hint
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "输入关键词搜索事件和待办",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else if (state.isSearching) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (state.events.isEmpty() && state.tasks.isEmpty()) {
                // No results
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.SearchOff,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "未找到结果",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Events section
                    if (state.events.isNotEmpty()) {
                        item {
                            Text(
                                "事件 (${state.events.size})",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        items(state.events, key = { "event_${it.id}" }) { event ->
                            EventSearchResultItem(
                                title = event.title,
                                subtitle = buildDateTimeRange(event.startTime, event.endTime, event.isAllDay, dateFormat),
                                color = event.color,
                                onClick = { onEventClick(event.id) }
                            )
                        }
                    }

                    // Tasks section
                    if (state.tasks.isNotEmpty()) {
                        item {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "待办 (${state.tasks.size})",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        items(state.tasks, key = { "task_${it.id}" }) { task ->
                            TaskSearchResultItem(
                                title = task.title,
                                status = task.status,
                                dueDate = task.dueDate,
                                dateFormat = dateFormat,
                                onClick = { onTaskClick(task.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EventSearchResultItem(
    title: String,
    subtitle: String,
    color: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(4.dp, 40.dp),
                color = androidx.compose.ui.graphics.Color(color),
                shape = MaterialTheme.shapes.small
            ) {}
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TaskSearchResultItem(
    title: String,
    status: Task.TaskStatus,
    dueDate: Long?,
    dateFormat: SimpleDateFormat,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (status) {
                    Task.TaskStatus.DONE -> Icons.Default.CheckCircle
                    Task.TaskStatus.IN_PROGRESS -> Icons.Default.RadioButtonChecked
                    Task.TaskStatus.TODO -> Icons.Default.RadioButtonUnchecked
                },
                contentDescription = null,
                tint = when (status) {
                    Task.TaskStatus.DONE -> MaterialTheme.colorScheme.primary
                    Task.TaskStatus.IN_PROGRESS -> MaterialTheme.colorScheme.tertiary
                    Task.TaskStatus.TODO -> MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (dueDate != null) {
                    Text(
                        text = "截止: ${dateFormat.format(Date(dueDate))}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun buildDateTimeRange(startTime: Long, endTime: Long, isAllDay: Boolean, format: SimpleDateFormat): String {
    if (isAllDay) {
        val dateStr = SimpleDateFormat("MM/dd", Locale.getDefault()).format(Date(startTime))
        return "全天 $dateStr"
    }
    return "${format.format(Date(startTime))} - ${format.format(Date(endTime))}"
}
