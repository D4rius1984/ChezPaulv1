// Fichier: BottomNavigationBar.kt
package com.chezpaul.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class BottomNavItem(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
)

@Composable
fun BottomNavigationBar(
    selectedRoute: String,
    onItemSelected: (String) -> Unit,
    onAddClick: () -> Unit
) {
    val marronChezPaul = Color(0xFF23190e)
    val jauneChezPaul = Color(0xFFFFD600)
    val blanc = Color.White

    NavigationBar(
        containerColor = marronChezPaul,
        tonalElevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(marronChezPaul)
    ) {
        val items = listOf(
            BottomNavItem("Accueil", Icons.Filled.Home, "accueil"),
            BottomNavItem("Tables", Icons.AutoMirrored.Filled.List, "tables"),
            BottomNavItem("Ajouter", Icons.Filled.Add, "ajouter"),
            BottomNavItem("Groupes", Icons.Filled.Groups, "groupes"),
            BottomNavItem("Paramètres", Icons.Filled.Settings, "settings")
        )

        items.forEachIndexed { index, item ->
            if (index == 2) {
                // Bouton + central custom
                Box(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    FloatingActionButton(
                        onClick = onAddClick,
                        containerColor = jauneChezPaul,
                        contentColor = marronChezPaul,
                        modifier = Modifier.size(62.dp),
                        elevation = FloatingActionButtonDefaults.elevation(0.dp)
                    ) {
                        Icon(
                            item.icon,
                            contentDescription = "Ajouter",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            } else {
                NavigationBarItem(
                    selected = selectedRoute == item.route,
                    onClick = { onItemSelected(item.route) },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if (selectedRoute == item.route) jauneChezPaul else blanc
                        )
                    },
                    label = {
                        Text(
                            item.label,
                            color = if (selectedRoute == item.route) jauneChezPaul else blanc
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = jauneChezPaul,
                        selectedTextColor = jauneChezPaul,
                        unselectedIconColor = blanc,
                        unselectedTextColor = blanc,
                        indicatorColor = marronChezPaul
                    )
                )
            }
        }
    }
}
