package com.chezpaul.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.chezpaul.ui.navigation.Screen
import com.chezpaul.ui.theme.ChezPaulColors

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val screen: Screen?  // null pour le bouton "Ajouter" central
)

@Composable
fun BottomNavigationBar(
    selectedScreen: Screen,
    onItemSelected: (Screen) -> Unit,
    onAddClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    NavigationBar(
        containerColor = ChezPaulColors.FondPrincipal,
        tonalElevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(ChezPaulColors.FondPrincipal)
    ) {
        val items = listOf(
            BottomNavItem("Accueil", Icons.Filled.Home, Screen.Accueil),
            BottomNavItem("Tables", Icons.AutoMirrored.Filled.List, Screen.Tables),
            BottomNavItem("Ajouter", Icons.Filled.Add, null),
            BottomNavItem("Modifier", Icons.Filled.Edit, Screen.Modifier),
            BottomNavItem("Paramètres", Icons.Filled.Settings, Screen.Settings)
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
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onAddClick()
                        },
                        containerColor = ChezPaulColors.JauneMenu,
                        contentColor = ChezPaulColors.TexteNoir,
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
                val isSelected = item.screen == selectedScreen
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        item.screen?.let { onItemSelected(it) }
                    },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if (isSelected) ChezPaulColors.JauneMenu else ChezPaulColors.TexteBlanc
                        )
                    },
                    label = {
                        Text(
                            item.label,
                            color = if (isSelected) ChezPaulColors.JauneMenu else ChezPaulColors.TexteBlanc
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ChezPaulColors.JauneMenu,
                        selectedTextColor = ChezPaulColors.JauneMenu,
                        unselectedIconColor = ChezPaulColors.TexteBlanc,
                        unselectedTextColor = ChezPaulColors.TexteBlanc,
                        indicatorColor = ChezPaulColors.FondPrincipal
                    )
                )
            }
        }
    }
}
