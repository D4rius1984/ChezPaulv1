package com.chezpaul.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chezpaul.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SettingsScreen(
    viewModel: MainViewModel = viewModel(),
    onExport: () -> Unit = {} // ou export logiquement dans le viewModel
) {
    val jauneChezPaul = Color(0xFFFFD600)
    val fondChezPaul = Color(0xFF23190e)

    // On récupère les states du viewModel
    val isDarkTheme by viewModel.isDarkTheme
    val platsSpeciauxState by viewModel.platsSpeciauxState
    val ravigoteNotif by viewModel.ravigoteNotif
    val historiqueServices = viewModel.commandes

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

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp)
        ) {
            // ----- SECTION : Thème -----
            item {
                SectionHeader("Apparence", jauneChezPaul)
                SettingSwitchRow(
                    title = "Thème sombre",
                    subtitle = "Active le mode sombre pour toute l’application",
                    checked = isDarkTheme,
                    onCheckedChange = { viewModel.isDarkTheme.value = it },
                    accentColor = jauneChezPaul
                )
            }

            // ----- SECTION : Fin de service -----
            item {
                SectionHeader("Service", jauneChezPaul)
                SettingButtonRow(
                    title = "Fin de service",
                    subtitle = "Clôture le service en cours",
                    icon = Icons.Filled.ExitToApp,
                    onClick = { viewModel.resetAll() }, // Reset global !
                    accentColor = jauneChezPaul
                )
            }

            // ----- SECTION : Export -----
            item {
                SectionHeader("Archivage", jauneChezPaul)
                SettingButtonRow(
                    title = "Exporter commandes (.csv)",
                    subtitle = "Génère un fichier de toutes les commandes du service",
                    icon = Icons.Filled.Download,
                    onClick = onExport,
                    accentColor = jauneChezPaul
                )
            }

            // ----- SECTION : Plats spéciaux -----
            item {
                SectionHeader("Menu du jour", jauneChezPaul)
                SettingSwitchRow(
                    title = "Tête de veau dispo (manuel)",
                    subtitle = "Afficher/masquer le plat dans la prise de commande",
                    checked = platsSpeciauxState,
                    onCheckedChange = { viewModel.platsSpeciauxState.value = it },
                    accentColor = jauneChezPaul
                )
            }

            // ----- SECTION : Notif ravigote -----
            item {
                SectionHeader("Notifications", jauneChezPaul)
                SettingSwitchRow(
                    title = "Notif ravigote",
                    subtitle = "Avertir quand une ravigote est sélectionnée",
                    checked = ravigoteNotif,
                    onCheckedChange = { viewModel.ravigoteNotif.value = it },
                    accentColor = jauneChezPaul
                )
            }

            // ----- SECTION : Historique -----
            item {
                SectionHeader("Historique des services", jauneChezPaul)
            }
            items(historiqueServices) { service ->
                HistoriqueItem(service, accentColor = jauneChezPaul)
            }
            item { Spacer(Modifier.height(32.dp)) }
        }
    }
}

// Les autres composables restent identiques :
@Composable
fun SectionHeader(title: String, accentColor: Color) {
    Text(
        text = title,
        color = accentColor,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        modifier = Modifier.padding(start = 24.dp, top = 22.dp, bottom = 2.dp)
    )
}

@Composable
fun SettingSwitchRow(
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    accentColor: Color
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 6.dp)
            .heightIn(min = 54.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Medium, fontSize = 17.sp, color = Color.White)
            if (!subtitle.isNullOrEmpty())
                Text(subtitle, fontSize = 13.sp, color = Color(0xFFB6B6B6))
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = accentColor,
                checkedTrackColor = accentColor.copy(alpha = 0.5f),
                uncheckedThumbColor = Color(0xFF666666),
                uncheckedTrackColor = Color(0xFF424242)
            )
        )
    }
    HorizontalDivider(modifier = Modifier.padding(horizontal = 18.dp), color = Color(0x30FFFFFF))
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
            .padding(horizontal = 18.dp, vertical = 6.dp)
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

@Composable
fun HistoriqueItem(commande: com.chezpaul.model.Commande, accentColor: Color) {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
    val date = sdf.format(Date())

    Card(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 7.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2112))
    ) {
        Column(Modifier.padding(10.dp)) {
            Text("Date : $date", fontSize = 13.sp, color = accentColor, fontWeight = FontWeight.Bold)
            Text(
                "Table : ${commande.numeroTable} (${commande.nombreCouverts} couverts)",
                fontWeight = FontWeight.Medium, fontSize = 15.sp, color = Color.White
            )
            Text("Plats : " + commande.plats.joinToString { it.nom }, color = Color.White, fontSize = 14.sp)
            Text("Boissons : " + commande.boissons.joinToString { it.nom }, color = Color.White, fontSize = 14.sp)
            // Ajoute ici ravigote si besoin
        }
    }
}
