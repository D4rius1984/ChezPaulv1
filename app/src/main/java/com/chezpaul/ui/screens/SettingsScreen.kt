package com.chezpaul.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chezpaul.viewmodel.MainViewModel

@Composable
fun SettingsScreen(
    viewModel: MainViewModel = viewModel()
) {
    val jauneChezPaul = Color(0xFFFFD600)
    val fondChezPaul = Color(0xFF23190e)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(fondChezPaul)
            .padding(horizontal = 0.dp, vertical = 0.dp)
    ) {
        // Titre principal
        Text(
            "Paramètres",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = jauneChezPaul,
            modifier = Modifier.padding(start = 24.dp, top = 36.dp, bottom = 12.dp)
        )

        Spacer(Modifier.height(22.dp))
        SectionHeader("Service", jauneChezPaul)
        SettingButtonRow(
            title = "Fin de service",
            subtitle = "Clôture le service en cours et efface toutes les tables",
            icon = Icons.Filled.ExitToApp,
            onClick = { viewModel.resetAll() },
            accentColor = jauneChezPaul
        )
    }
}

@Composable
fun SectionHeader(title: String, accentColor: Color) {
    Text(
        text = title,
        color = accentColor,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        modifier = Modifier.padding(start = 24.dp, top = 0.dp, bottom = 2.dp)
    )
}

@Composable
fun SettingButtonRow(
    title: String,
    subtitle: String? = null,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    accentColor: Color
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 12.dp)
            .heightIn(min = 54.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Medium, fontSize = 17.sp, color = Color.White)
            if (!subtitle.isNullOrEmpty())
                Text(subtitle, fontSize = 13.sp, color = Color(0xFFB6B6B6))
        }
        IconButton(onClick = onClick) {
            Icon(icon, contentDescription = null, tint = accentColor)
        }
    }
    HorizontalDivider(modifier = Modifier.padding(horizontal = 18.dp), color = Color(0x30FFFFFF))
}
