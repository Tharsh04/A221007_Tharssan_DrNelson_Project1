package com.example.a221007_tharssan_drnelson_project1.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomNav(
    currentView: String,
    onNavigate: (String) -> Unit
) {

    val navItems = listOf(
        NavItem("home", "Home", Icons.Default.Home),
        NavItem("history", "History", Icons.Default.History),
        NavItem("profile", "Profile", Icons.Default.Person)
    )

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        navItems.forEach { item ->
            val isActive = currentView == item.id
            NavigationBarItem(
                selected = isActive,
                onClick = { onNavigate(item.id) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (isActive) Color(0xFFEA580C) else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 12.sp,
                        color = if (isActive) Color(0xFFEA580C) else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFEA580C),
                    selectedTextColor = Color(0xFFEA580C),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

data class NavItem(
    val id: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)