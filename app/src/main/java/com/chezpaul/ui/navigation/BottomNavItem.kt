package com.chezpaul.ui.navigation

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String,
)

// Tu peux adapter les icônes et les routes comme tu veux
val bottomNavItems = listOf(
    BottomNavItem("Accueil", Icons.Default.Home, "accueil"),
    BottomNavItem("Tables", Icons.Default.TableChart, "tables"),
    BottomNavItem("Commande", Icons.Default.ShoppingCart, "commande"),
    BottomNavItem("Fermer Service", Icons.Default.Lock, "fermer_service"),
    BottomNavItem("Options", Icons.Default.Settings, "options"),
)
