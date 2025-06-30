package com.chezpaul.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chezpaul.model.Commande
import com.chezpaul.model.CategorieBoisson
import com.chezpaul.ui.components.*
import com.chezpaul.ui.theme.ChezPaulColors

@Composable
fun AccueilScreen(
    commandesList: List<Commande>
) {
    val totalCouverts = commandesList.sumOf { it.nombreCouverts }
    // Associe enum et label d'affichage
    val categories = listOf(
        CategorieBoisson.APEROS to "Apéros",
        CategorieBoisson.VINS to "Vins",
        CategorieBoisson.DIGESTIFS to "Digestifs",
        CategorieBoisson.BIERES to "Bières",
        CategorieBoisson.SOFTS to "Softs"
    )
    // Compte en utilisant les enums (plus d'erreur d'accent)
    val boissonsParCategorie = categories.associate { (catEnum, _) ->
        catEnum to commandesList.flatMap { it.boissons }
            .filter { it.categorie == catEnum }
            .sumOf { it.quantite }
    }
    val nombreRavigotes = commandesList.count { commande ->
        commande.plats.any { it.contientRavigote || it.nom.contains("tête de veau", ignoreCase = true) }
    }

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
                Text(
                    "Tables ouvertes (${commandesList.size})",
                    color = ChezPaulColors.TexteBlanc,
                    style = MaterialTheme.typography.titleMedium
                )
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