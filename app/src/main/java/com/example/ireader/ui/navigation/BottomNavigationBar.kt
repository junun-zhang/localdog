package com.example.ireader.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ireader.R

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        Screen.Bookshelf,
        Screen.Store,
        Screen.Profile
    )

    NavigationBar {
        val backStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry.value?.destination?.route

        items.forEach { screen ->
            val titleRes = when (screen) {
                Screen.Bookshelf -> R.string.bookshelf
                Screen.Store -> R.string.store
                Screen.Profile -> R.string.profile
                is Screen.Reader -> R.string.bookshelf
            }
            val icon = when (screen) {
                Screen.Bookshelf -> Icons.Default.Book
                Screen.Store -> Icons.Default.Store
                Screen.Profile -> Icons.Default.Person
                is Screen.Reader -> Icons.Default.Book
            }
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = stringResource(titleRes)) },
                label = { Text(stringResource(titleRes)) },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
