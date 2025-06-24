package com.chezpaul.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AccueilScreen() {
    CenteredIconWithText(
        icon = Icons.Default.Home,
        text = "Accueil"
    )
}

@Composable
fun TablesScreen() {
    CenteredIconWithText(
        icon = Icons.Default.TableChart,
        text = "Tables"
    )
}

@Composable
fun FermerServiceScreen() {
    CenteredIconWithText(
        icon = Icons.Default.Lock,
        text = "Fermer Service"
    )
}

@Composable
fun OptionsScreen() {
    CenteredIconWithText(
        icon = Icons.Default.Settings,
        text = "Options"
    )
}

@Composable
private fun CenteredIconWithText(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(72.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = text, fontSize = 24.sp)
        }
    }
}
