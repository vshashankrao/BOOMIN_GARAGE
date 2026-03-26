package com.boomingarage.android.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Garage
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: Route
)

val customerNavItems = listOf(
    BottomNavItem("Home", Icons.Default.Home, Route.Home),
    BottomNavItem("Map", Icons.Default.Map, Route.Map),
    BottomNavItem("Bookings", Icons.Default.CalendarMonth, Route.BookingHistory),
    BottomNavItem("Profile", Icons.Default.Person, Route.Profile)
)

val operatorNavItems = listOf(
    BottomNavItem("Dashboard", Icons.Default.Dashboard, Route.OperatorDashboard),
    BottomNavItem("Bookings", Icons.Default.CalendarMonth, Route.ManageBookings),
    BottomNavItem("Bays", Icons.Default.Garage, Route.BayManagement),
    BottomNavItem("Stats", Icons.Default.BarChart, Route.RevenueStats)
)

@Composable
fun BottomNavBar(
    items: List<BottomNavItem>,
    currentRoute: String?,
    onItemClick: (Route) -> Unit
) {
    NavigationBar {
        items.forEach { item ->
            val selected = currentRoute == item.route::class.qualifiedName
            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}
