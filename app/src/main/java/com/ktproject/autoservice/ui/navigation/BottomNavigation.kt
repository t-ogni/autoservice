package com.ktproject.autoservice.ui.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

data class BottomNavItem(
    // Text below icon
    val label: String,
    // Icon
    val icon: ImageVector,
    // Route to the specific screen
    val route:String,
)

object Constants {
    val BottomNavItems = listOf(
        // Home screen
        BottomNavItem(
            label = "Главная",
            icon = Icons.Filled.Home,
            route = "home"
        ),
        // Search screen
        BottomNavItem(
            label = "Услуги",
            icon = Icons.Filled.Build,
            route = "services"
        ),
        // Profile screen
        BottomNavItem(
            label = "Профиль",
            icon = Icons.Filled.Person,
            route = "profile"
        )
    )
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val colorScheme = MaterialTheme.colorScheme
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = colorScheme.surface,
        tonalElevation = 4.dp
    ) {
        Constants.BottomNavItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    if (currentRoute != navItem.route) {
                        navController.navigate(navItem.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.label
                    )
                },
                label = {
                    Text(text = navItem.label)
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colorScheme.primary,
                    unselectedIconColor = colorScheme.onSurface.copy(alpha = 0.6f),
                    selectedTextColor = colorScheme.primary,
                    indicatorColor = colorScheme.primary.copy(alpha = 0.12f)
                )
            )
        }
    }
}
