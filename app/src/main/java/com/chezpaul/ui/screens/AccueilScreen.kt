package com.chezpaul.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Euro
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TableBar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chezpaul.model.CategorieBoisson
import com.chezpaul.ui.components.*
import com.chezpaul.ui.theme.ChezPaulColors
import com.chezpaul.viewmodel.CommandeViewModel

@Composable
fun AccueilScreen(
    commandeViewModel: CommandeViewModel
) {
    val commandesList by commandeViewModel.commandesList
    val totalCouverts = commandeViewModel.totalCouverts
    val boissonsParCategorie = commandeViewModel.boissonsParCategorie
    val nombreRavigotes = commandeViewModel.nombreRavigotes

    val categories = listOf(
        CategorieBoisson.APEROS to "Apéros",
        CategorieBoisson.VINS_FONTAINE to "Vins Fontaine",
        CategorieBoisson.VINS_BOUTEILLES to "Vins Bouteilles",
        CategorieBoisson.DIGESTIFS to "Digestifs",
        CategorieBoisson.BIERES to "Bières",
        CategorieBoisson.SOFTS to "Softs"
    )

    ChezPaulScreen(title = "Accueil") {

        // ROW : Couverts + Ravigote côte à côte
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ChezPaulCard(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.People,
                        contentDescription = null,
                        tint = ChezPaulColors.JauneMenu,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            "Couverts",
                            color = ChezPaulColors.TexteBlanc,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            "$totalCouverts",
                            color = ChezPaulColors.JauneMenu,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            ChezPaulCard(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = ChezPaulColors.JauneMenu,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            "Ravigote",
                            color = ChezPaulColors.TexteBlanc,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            "$nombreRavigotes",
                            color = ChezPaulColors.JauneMenu,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // CARD : Boissons par catégorie
        ChezPaulCard {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocalDrink,
                        contentDescription = null,
                        tint = ChezPaulColors.JauneMenu,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        "Boissons commandées",
                        color = ChezPaulColors.TexteBlanc,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(Modifier.height(8.dp))
                categories.forEach { (catEnum, catAffichage) ->
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(catAffichage, color = ChezPaulColors.TexteBlanc)
                        Text(
                            "${boissonsParCategorie[catEnum] ?: 0}",
                            color = ChezPaulColors.JauneMenu,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(3.dp))
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // CARD : Chiffre d'Affaires
        ChezPaulCard {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.Euro,
                        contentDescription = null,
                        tint = ChezPaulColors.JauneMenu,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        "Chiffre d'Affaires",
                        color = ChezPaulColors.TexteBlanc,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        "%.2f €".format(commandeViewModel.caTotal),
                        color = ChezPaulColors.JauneMenu,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Plats (menu 32€)", color = ChezPaulColors.TexteBlanc)
                    Text(
                        "%.2f €".format(commandeViewModel.caPlats),
                        color = ChezPaulColors.JauneMenu,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(3.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Boissons", color = ChezPaulColors.TexteBlanc)
                    Text(
                        "%.2f €".format(commandeViewModel.caBoissons),
                        color = ChezPaulColors.JauneMenu,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(3.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Ticket moyen", color = ChezPaulColors.TexteBlanc)
                    Text(
                        if (totalCouverts > 0) "%.2f €".format(commandeViewModel.caTotal / totalCouverts)
                        else "— €",
                        color = ChezPaulColors.JauneMenu,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // CARD : Tables ouvertes
        ChezPaulCard {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.TableBar,
                        contentDescription = null,
                        tint = ChezPaulColors.JauneMenu,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        "Tables ouvertes",
                        color = ChezPaulColors.TexteBlanc,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        "${commandesList.size}",
                        color = ChezPaulColors.JauneMenu,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(6.dp))
                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState())
                ) {
                    commandesList.forEach { cmd ->
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Table ${cmd.numeroTable} • ${cmd.nombreCouverts} cvts",
                                    color = ChezPaulColors.TexteBlanc,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                if (cmd.isGroupe) {
                                    Spacer(Modifier.width(8.dp))
                                    ChezPaulBadge(
                                        text = "Groupe",
                                        backgroundColor = ChezPaulColors.JauneMenu,
                                        textColor = ChezPaulColors.TexteNoir
                                    )
                                }
                            }
                            if (cmd.plats.any { it.contientRavigote || it.nom.contains("tête de veau", true) }) {
                                Text("⚡", color = ChezPaulColors.JauneMenu, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                        ChezPaulDivider(modifier = Modifier.padding(vertical = 1.dp))
                    }
                }
            }
        }
    }
}
