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
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.calsync.app.R
import com.calsync.app.ui.calendar.day.DayScreen
import com.calsync.app.ui.calendar.month.MonthScreen
import com.calsync.app.ui.calendar.schedule.ScheduleScreen
import com.calsync.app.ui.calendar.week.WeekScreen
import com.calsync.app.ui.event.EventDetailScreen
import com.calsync.app.ui.event.EventEditScreen
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
                            if (currentRoute in listOf("month", "week", "day", "schedule", "tasks")) {
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
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { navController.navigate("event/create") },
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "添加事件")
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "month",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("month") {
                            MonthScreen(
                                onNavigateToDay = { date ->
                                    navController.navigate("day/$date")
                                }
                            )
                        }
                        composable("week") { WeekScreen() }
                        // Day route without parameter (defaults to today)
                        composable("day") { DayScreen(
                                onEventClick = { eventId -> navController.navigate("event/$eventId") }
                            ) }
                        // Day route with date parameter
                        composable(
                            route = "day/{date}",
                            arguments = listOf(navArgument("date") { type = androidx.navigation.NavType.LongType })
                        ) { backStackEntry ->
                            val date = backStackEntry.arguments?.getLong("date") ?: System.currentTimeMillis()
                            DayScreen(
                                selectedDate = date,
                                onEventClick = { eventId -> navController.navigate("event/$eventId") }
                            )
                        }
                        composable("schedule") { ScheduleScreen() }
                        composable("tasks") { TasksScreen() }
                        composable("event/create") {
                            EventEditScreen(
                                eventId = null,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable(
                            route = "event/{eventId}",
                            arguments = listOf(navArgument("eventId") { type = androidx.navigation.NavType.StringType })
                        ) { backStackEntry ->
                            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
                            EventDetailScreen(
                                eventId = eventId,
                                onNavigateBack = { navController.popBackStack() },
                                onEdit = { navController.navigate("event/edit/$eventId") },
                                onEditSingleOccurrence = { newEventId ->
                                    navController.navigate("event/edit/$newEventId")
                                }
                            )
                        }
                        composable(
                            route = "event/edit/{eventId}",
                            arguments = listOf(navArgument("eventId") { type = androidx.navigation.NavType.StringType })
                        ) { backStackEntry ->
                            val eventId = backStackEntry.arguments?.getString("eventId")
                            EventEditScreen(
                                eventId = eventId,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
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
