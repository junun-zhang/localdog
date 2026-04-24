package com.calsync.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.navigation.compose.*
import com.calsync.app.R
import com.calsync.app.ui.calendar.day.DayScreen
import com.calsync.app.ui.calendar.month.MonthScreen
import com.calsync.app.ui.calendar.schedule.ScheduleScreen
import com.calsync.app.ui.calendar.week.WeekScreen
import com.calsync.app.ui.task.TasksScreen
import com.calsync.app.ui.theme.CalSyncTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalSyncTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentRoute = navBackStackEntry?.destination?.route
                            listOf(
                                BottomNavItem.Month,
                                BottomNavItem.Week,
                                BottomNavItem.Day,
                                BottomNavItem.Schedule,
                                BottomNavItem.Tasks
                            ).forEach { item ->
                                NavigationBarItem(
                                    icon = { Icon(item.icon, contentDescription = null) },
                                    label = { Text(stringResource(item.labelRes)) },
                                    selected = currentRoute == item.route,
                                    onClick = {
                                        navController.navigate(item.route) {
                                            popUpTo("month") { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { /* TODO: 创建事件 */ },
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "添加")
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "month",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("month") { MonthScreen() }
                        composable("week") { WeekScreen() }
                        composable("day") { DayScreen() }
                        composable("schedule") { ScheduleScreen() }
                        composable("tasks") { TasksScreen() }
                    }
                }
            }
        }
    }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val labelRes: Int) {
    object Month : BottomNavItem("month", Icons.Default.CalendarMonth, R.string.nav_month)
    object Week : BottomNavItem("week", Icons.Default.ViewWeek, R.string.nav_week)
    object Day : BottomNavItem("day", Icons.Default.CalendarToday, R.string.nav_day)
    object Schedule : BottomNavItem("schedule", Icons.Default.CalendarToday, R.string.nav_schedule)
    object Tasks : BottomNavItem("tasks", Icons.Default.Checklist, R.string.nav_tasks)
}
