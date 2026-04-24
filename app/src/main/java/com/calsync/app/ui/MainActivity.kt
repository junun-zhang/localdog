package com.calsync.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.calsync.app.R
import com.calsync.app.ui.navigation.CalSyncNavGraph
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
                        if (currentRoute in listOf(
                            "month", "week", "day", "schedule", "tasks"
                        )) {
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
                    CalSyncNavGraph(
                        navController = navController,
                        modifier = androidx.compose.ui.Modifier.padding(innerPadding)
                    )
                }
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}

sealed class BottomNavItem(
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val labelRes: Int
) {
    object Month : BottomNavItem(
        "month",
        androidx.compose.material.icons.Icons.Default.CalendarMonth,
        R.string.nav_month
    )
    object Week : BottomNavItem(
        "week",
        androidx.compose.material.icons.Icons.Default.ViewWeek,
        R.string.nav_week
    )
    object Day : BottomNavItem(
        "day",
        androidx.compose.material.icons.Icons.Default.CalendarToday,
        R.string.nav_day
    )
    object Schedule : BottomNavItem(
        "schedule",
        androidx.compose.material.icons.Icons.Default.FormatListBulleted,
        R.string.nav_schedule
    )
    object Tasks : BottomNavItem(
        "tasks",
        androidx.compose.material.icons.Icons.Default.Checklist,
        R.string.nav_tasks
    )
}
