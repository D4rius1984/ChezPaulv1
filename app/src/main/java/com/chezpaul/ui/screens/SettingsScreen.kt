package com.chezpaul.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chezpaul.ui.components.*
import com.chezpaul.ui.theme.ChezPaulColors
import com.chezpaul.viewmodel.CommandeViewModel

@Composable
fun SettingsScreen(viewModel: CommandeViewModel) {
    var tdvDispo by remember { mutableStateOf(false) }
    var notifRavigote by remember { mutableStateOf(false) }
    var isDarkMode by remember { mutableStateOf(true) }

    ChezPaulScreen(title = "Paramètres") {
        ChezPaulCard {
            // Section Apparence
            Text(
                "Apparence",
                color = ChezPaulColors.JauneMenu,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Thème sombre",
                        color = ChezPaulColors.TexteBlanc,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        "Active le mode sombre pour toute l'application",
                        color = ChezPaulColors.TexteGris,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { isDarkMode = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = ChezPaulColors.JauneMenu,
                        uncheckedThumbColor = ChezPaulColors.TexteGris,
                        checkedTrackColor = ChezPaulColors.OrangeMenu.copy(alpha = 0.5f),
                        uncheckedTrackColor = ChezPaulColors.FondCard
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ChezPaulCard {
            // Section Service
            Text(
                "Service",
                color = ChezPaulColors.JauneMenu,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* Action fin de service */ }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Fin de service",
                        color = ChezPaulColors.TexteBlanc,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        "Clôture le service en cours",
                        color = ChezPaulColors.TexteGris,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Text(
                    "→",
                    color = ChezPaulColors.JauneMenu,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ChezPaulCard {
            // Section Archivage
            Text(
                "Archivage",
                color = ChezPaulColors.JauneMenu,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* Action export */ }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Exporter commandes (.csv)",
                        color = ChezPaulColors.TexteBlanc,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        "Génère un fichier de toutes les commandes du service",
                        color = ChezPaulColors.TexteGris,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Text(
                    "↓",
                    color = ChezPaulColors.JauneMenu,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ChezPaulCard {
            // Section Menu du jour
            Text(
                "Menu du jour",
                color = ChezPaulColors.JauneMenu,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Tête de veau dispo (manuel)",
                        color = ChezPaulColors.TexteBlanc,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        "Afficher/masquer le plat dans la prise de commande",
                        color = ChezPaulColors.TexteGris,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Switch(
                    checked = tdvDispo,
                    onCheckedChange = { tdvDispo = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = ChezPaulColors.JauneMenu,
                        uncheckedThumbColor = ChezPaulColors.TexteGris,
                        checkedTrackColor = ChezPaulColors.OrangeMenu.copy(alpha = 0.5f),
                        uncheckedTrackColor = ChezPaulColors.FondCard
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ChezPaulCard {
            // Section Notifications
            Text(
                "Notifications",
                color = ChezPaulColors.JauneMenu,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Notif ravigote",
                        color = ChezPaulColors.TexteBlanc,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        "Avertir quand une ravigote est sélectionnée",
                        color = ChezPaulColors.TexteGris,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Switch(
                    checked = notifRavigote,
                    onCheckedChange = { notifRavigote = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = ChezPaulColors.JauneMenu,
                        uncheckedThumbColor = ChezPaulColors.TexteGris,
                        checkedTrackColor = ChezPaulColors.OrangeMenu.copy(alpha = 0.5f),
                        uncheckedTrackColor = ChezPaulColors.FondCard
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ChezPaulCard {
            // Section Historique
            Text(
                "Historique des services",
                color = ChezPaulColors.JauneMenu,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Column {
                ServiceHistoryItem("29/06/2025", "234€", 45)
                ChezPaulDivider(modifier = Modifier.padding(vertical = 8.dp))
                ServiceHistoryItem("28/06/2025", "567€", 78)
                ChezPaulDivider(modifier = Modifier.padding(vertical = 8.dp))
                ServiceHistoryItem("27/06/2025", "432€", 62)
            }
        }
    }
}

@Composable
private fun ServiceHistoryItem(date: String, total: String, couverts: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Voir détails */ }
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                "Date : $date",
                color = ChezPaulColors.TexteBlanc,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "$couverts couverts",
                color = ChezPaulColors.TexteGris,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(
            total,
            color = ChezPaulColors.JauneMenu,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}