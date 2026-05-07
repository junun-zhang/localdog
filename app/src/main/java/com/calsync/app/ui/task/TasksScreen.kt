package com.calsync.app.ui.task

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.calsync.app.domain.model.Task
import com.calsync.app.domain.model.Task.Priority
import com.calsync.app.domain.model.Task.TaskStatus
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TasksScreen(
    onCreateTask: () -> Unit = {},
    onEditTask: ((String) -> Unit)? = null,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val filteredTasks = viewModel.getFilteredTasks()
    var selectedTaskIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    val isMultiSelect = selectedTaskIds.isNotEmpty()

    fun exitMultiSelect() {
        selectedTaskIds = emptySet()
    }

    fun toggleSelection(taskId: String) {
        selectedTaskIds = if (selectedTaskIds.contains(taskId)) {
            selectedTaskIds - taskId
        } else {
            selectedTaskIds + taskId
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 2.dp
            ) {
                Column {
                    if (isMultiSelect) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(start = 4.dp, end = 4.dp, top = 8.dp, bottom = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { exitMultiSelect() }) {
                                Icon(Icons.Default.Close, contentDescription = "\u53d6\u6d88\u591a\u9009")
                            }
                            Text(
                                text = "\u5df2\u9009\u62e9 ${selectedTaskIds.size} \u9879",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(onClick = {
                                viewModel.batchCompleteTasks(selectedTaskIds.toList())
                                exitMultiSelect()
                            }) {
                                Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("\u5b8c\u6210", fontSize = 13.sp)
                            }
                            TextButton(onClick = {
                                val selectedTasks = filteredTasks.filter { it.id in selectedTaskIds }
                                viewModel.batchDeleteTasks(selectedTasks)
                                exitMultiSelect()
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.error)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("\u5220\u9664", fontSize = 13.sp, color = MaterialTheme.colorScheme.error)
                            }
                        }
                    } else {
                        Text(
                            text = "\u5f85\u529e\u4e8b\u9879",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
                        )
                    }
                    FilterTabs(
                        activeFilter = state.activeFilter,
                        onFilterChange = { viewModel.setFilter(it) }
                    )
                }
            }

            // Task list
            if (filteredTasks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (state.activeFilter) {
                            TaskFilter.ALL -> "\u6682\u65e0\u5f85\u529e\n\u70b9\u51fb\u53f3\u4e0b\u89d2 + \u6dfb\u52a0"
                            TaskFilter.TODO -> "\u6ca1\u6709\u5f85\u529e\u4e8b\u9879"
                            TaskFilter.IN_PROGRESS -> "\u6ca1\u6709\u8fdb\u884c\u4e2d\u7684\u4e8b\u9879"
                            TaskFilter.DONE -> "\u6ca1\u6709\u5df2\u5b8c\u6210\u7684\u4e8b\u9879"
                        },
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 24.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(filteredTasks, key = { it.id }) { task ->
                        TaskCard(
                            task = task,
                            isSelected = selectedTaskIds.contains(task.id),
                            isMultiSelectMode = isMultiSelect,
                            onToggleStatus = { viewModel.toggleTaskStatus(task) },
                            onClick = {
                                if (isMultiSelect) {
                                    toggleSelection(task.id)
                                } else {
                                    onEditTask?.invoke(task.id)
                                }
                            },
                            onLongClick = {
                                if (!isMultiSelect) {
                                    selectedTaskIds = setOf(task.id)
                                }
                            },
                            onDelete = { viewModel.deleteTask(task) }
                        )
                    }
                }
            }
        }

        // FAB
        if (!isMultiSelect) {
            FloatingActionButton(
                onClick = onCreateTask,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "\u6dfb\u52a0\u5f85\u529e")
            }
        }
    }
}

@Composable
fun FilterTabs(activeFilter: TaskFilter, onFilterChange: (TaskFilter) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        listOf(
            TaskFilter.ALL to "\u5168\u90e8",
            TaskFilter.TODO to "\u5f85\u529e",
            TaskFilter.IN_PROGRESS to "\u8fdb\u884c\u4e2d",
            TaskFilter.DONE to "\u5df2\u5b8c\u6210"
        ).forEach { (filter, label) ->
            FilterChip(
                selected = activeFilter == filter,
                onClick = { onFilterChange(filter) },
                label = { Text(label, fontSize = 13.sp) }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskCard(
    task: Task,
    isSelected: Boolean = false,
    isMultiSelectMode: Boolean = false,
    onToggleStatus: () -> Unit,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {},
    onDelete: () -> Unit
) {
    val bgColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            if (isMultiSelectMode) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onClick() },
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            } else {
                IconButton(onClick = onToggleStatus, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = if (task.status == TaskStatus.DONE) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        contentDescription = if (task.status == TaskStatus.DONE) "\u5df2\u5b8c\u6210" else "\u6807\u8bb0\u5b8c\u6210",
                        tint = if (task.status == TaskStatus.DONE) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = task.title, fontSize = 15.sp, fontWeight = FontWeight.Medium,
                        textDecoration = if (task.status == TaskStatus.DONE) TextDecoration.LineThrough else TextDecoration.None,
                        color = if (task.status == TaskStatus.DONE) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                        maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    PriorityBadge(task.priority)
                }
                if (task.dueDate != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    val sdf = SimpleDateFormat("MM/dd", Locale.getDefault())
                    val isOverdue = task.dueDate < System.currentTimeMillis() && task.status != TaskStatus.DONE
                    Text(
                        text = "\u622a\u6b62: " + sdf.format(task.dueDate),
                        fontSize = 12.sp,
                        color = if (isOverdue) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (!isMultiSelectMode) {
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "\u5220\u9664", modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun PriorityBadge(priority: Priority) {
    val (color, label) = when (priority) {
        Priority.NONE -> Pair(Color.Gray, "")
        Priority.LOW -> Pair(Color(0xFF4CAF50), "\u4f4e")
        Priority.MEDIUM -> Pair(Color(0xFFFF9800), "\u4e2d")
        Priority.HIGH -> Pair(Color(0xFFF44336), "\u9ad8")
    }
    if (priority != Priority.NONE) {
        Surface(shape = MaterialTheme.shapes.small, color = color.copy(alpha = 0.15f)) {
            Text(text = label, fontSize = 11.sp, color = color, fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
        }
    }
}
