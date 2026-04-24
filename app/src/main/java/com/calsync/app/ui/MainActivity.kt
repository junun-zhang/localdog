package com.calsync.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ViewWeek
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Checklist
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.calsync.app.R
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
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route
                        if (currentRoute in listOf("month", "week", "day", "schedule", "tasks")) {
                            NavigationBar {
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
                    }
                ) { innerPadding ->
                    Text(
                        text = "CalSync - 共享日历",
                        modifier = androidx.compose.ui.Modifier.padding(innerPadding),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }
    }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val labelRes: Int) {
    object Month : BottomNavItem("month", Icons.Default.CalendarMonth, R.string.nav_month)
    object Week : BottomNavItem("week", Icons.Default.ViewWeek, R.string.nav_week)
    object Day : BottomNavItem("day", Icons.Default.CalendarToday, R.string.nav_day)
    object Schedule : BottomNavItem("schedule", Icons.Default.FormatListBulleted, R.string.nav_schedule)
    object Tasks : BottomNavItem("tasks", Icons.Default.Checklist, R.string.nav_tasks)
}
