package com.chezpaul.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
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
import com.chezpaul.viewmodel.AccueilViewModel

@Composable
fun AccueilScreen(
    accueilViewModel: AccueilViewModel
) {
    // Observer les données depuis le ViewModel
    val commandesList by accueilViewModel.commandesList
    val totalCouverts = accueilViewModel.totalCouverts
    val boissonsParCategorie = accueilViewModel.boissonsParCategorie
    val nombreRavigotes = accueilViewModel.nombreRavigotes

    // Associe enum et label d'affichage
    val categories = listOf(
        CategorieBoisson.APEROS to "Apéros",
        CategorieBoisson.VINS_FONTAINE to "Vins Fontaine",
        CategorieBoisson.VINS_BOUTEILLES to "Vins Bouteilles",
        CategorieBoisson.DIGESTIFS to "Digestifs",
        CategorieBoisson.BIERES to "Bières",
        CategorieBoisson.SOFTS to "Softs"
    )

    ChezPaulScreen(title = "Accueil") {

        // CARD : Total couverts
        ChezPaulCard {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.People,
                    contentDescription = null,
                    tint = ChezPaulColors.JauneMenu,
                    modifier = Modifier.size(34.dp)
                )
                Spacer(Modifier.width(18.dp))
                Column {
                    Text(
                        "Couverts totaux",
                        color = ChezPaulColors.TexteBlanc,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "$totalCouverts",
                        color = ChezPaulColors.JauneMenu,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        // CARD : Boissons par catégorie
        ChezPaulCard {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocalDrink,
                        contentDescription = null,
                        tint = ChezPaulColors.JauneMenu,
                        modifier = Modifier.size(28.dp)
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
                    Spacer(Modifier.height(4.dp))
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        // CARD : Ravigote
        ChezPaulCard {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = ChezPaulColors.JauneMenu,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(
                        "Tables avec ravigote",
                        color = ChezPaulColors.TexteBlanc,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "$nombreRavigotes",
                        color = ChezPaulColors.JauneMenu,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(Modifier.height(22.dp))

        // CARD : Tables ouvertes (liste mini)
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
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(
                            "Tables ouvertes",
                            color = ChezPaulColors.TexteBlanc,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            "${commandesList.size}",
                            color = ChezPaulColors.JauneMenu,
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
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
                    ChezPaulDivider(modifier = Modifier.padding(vertical = 2.dp))
                }
            }
        }
    }
}