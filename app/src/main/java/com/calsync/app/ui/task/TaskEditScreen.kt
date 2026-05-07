package com.calsync.app.ui.task

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.calsync.app.domain.model.Task
import com.calsync.app.domain.model.Task.Priority
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditScreen(
    taskId: String? = null,
    onNavigateBack: () -> Unit,
    viewModel: TaskViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf<Long?>(null) }
    var priority by remember { mutableIntStateOf(Priority.MEDIUM.ordinal) }
    var isLoading by remember { mutableStateOf(taskId != null) }
    var isEditing by remember { mutableStateOf(false) }

    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }

    // Load existing task in edit mode
    LaunchedEffect(taskId) {
        if (taskId != null) {
            viewModel.getTaskById(taskId) { task ->
                if (task != null) {
                    title = task.title
                    description = task.description ?: ""
                    dueDate = task.dueDate
                    priority = task.priority.ordinal
                    isEditing = true
                }
                isLoading = false
            }
        }
    }

    if (showDatePicker) {
        val cal = Calendar.getInstance()
        dueDate?.let { cal.timeInMillis = it }
        DatePickerDialog(
            context,
            { _, year, month, day ->
                cal.set(year, month, day, 23, 59, 59)
                cal.set(Calendar.MILLISECOND, 0)
                dueDate = cal.timeInMillis
                showDatePicker = false
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "编辑待办" else "新建待办") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("\u2190", fontSize = 24.sp)
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            val effectiveTitle = title.ifBlank { "新待办" }
                            if (isEditing && taskId != null) {
                                viewModel.getTaskById(taskId) { existing ->
                                    if (existing != null) {
                                        val updated = existing.copy(
                                            title = effectiveTitle,
                                            description = description.ifEmpty { null },
                                            dueDate = dueDate,
                                            priority = Priority.entries.getOrElse(priority) { Priority.MEDIUM }
                                        )
                                        viewModel.updateTask(updated) { onNavigateBack() }
                                    } else onNavigateBack()
                                }
                            } else {
                                viewModel.createTask(
                                    title = effectiveTitle,
                                    description = description.ifEmpty { null },
                                    dueDate = dueDate,
                                    priority = Priority.entries.getOrElse(priority) { Priority.MEDIUM }
                                ) { onNavigateBack() }
                            }
                        },
                        enabled = true
                    ) {
                        Text("保存", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("待办标题") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("输入待办标题") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("描述（可选）") },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                placeholder = { Text("添加描述") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Due date
            Text("截止日期", fontSize = 15.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(onClick = { showDatePicker = true }) {
                    if (dueDate != null) {
                        val sdf = java.text.SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                        Text(sdf.format(dueDate!!))
                    } else {
                        Text("选择日期")
                    }
                }
                if (dueDate != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = { dueDate = null }) {
                        Text("清除", color = MaterialTheme.colorScheme.error)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Priority selector
            Text("优先级", fontSize = 15.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Priority.entries.forEachIndexed { index, p ->
                    FilterChip(
                        selected = priority == index,
                        onClick = { priority = index },
                        label = { Text(p.name, fontSize = 13.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
