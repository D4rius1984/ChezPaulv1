package com.chezpaul.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chezpaul.model.Commande
import com.chezpaul.model.CategorieBoisson

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
    // Compte en utilisant les enums (plus d’erreur d’accent)
    val boissonsParCategorie = categories.associate { (catEnum, _) ->
        catEnum to commandesList.flatMap { it.boissons }
            .filter { it.categorie == catEnum }
            .sumOf { it.quantite }
    }
    val nombreRavigotes = commandesList.count { commande ->
        commande.plats.any { it.contientRavigote || it.nom.contains("tête de veau", ignoreCase = true) }
    }

    val jauneMenu = Color(0xFFFFE066) // Jaune du menu

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF222222))
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            "Accueil",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // CARD : Total couverts
        DashboardCard {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.People, contentDescription = null, tint = jauneMenu, modifier = Modifier.size(34.dp))
                Spacer(Modifier.width(18.dp))
                Column {
                    Text("Couverts totaux", color = Color.White, style = MaterialTheme.typography.titleMedium)
                    Text(
                        "$totalCouverts",
                        color = jauneMenu,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Spacer(Modifier.height(18.dp))

        // CARD : Boissons par catégorie
        DashboardCard {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocalDrink, contentDescription = null, tint = jauneMenu, modifier = Modifier.size(28.dp))
                    Spacer(Modifier.width(10.dp))
                    Text("Boissons commandées", color = Color.White, style = MaterialTheme.typography.titleMedium)
                }
                Spacer(Modifier.height(8.dp))
                categories.forEach { (catEnum, catAffichage) ->
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(catAffichage, color = Color.White)
                        Text(
                            "${boissonsParCategorie[catEnum] ?: 0}",
                            color = jauneMenu,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                }
            }
        }
        Spacer(Modifier.height(18.dp))

        // CARD : Ravigote
        DashboardCard {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Star, contentDescription = null, tint = jauneMenu, modifier = Modifier.size(30.dp))
                Spacer(Modifier.width(14.dp))
                Column {
                    Text("Tables avec ravigote", color = Color.White, style = MaterialTheme.typography.titleMedium)
                    Text(
                        "$nombreRavigotes",
                        color = jauneMenu,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Spacer(Modifier.height(22.dp))

        // CARD : Tables ouvertes (liste mini)
        DashboardCard {
            Column {
                Text("Tables ouvertes (${commandesList.size})", color = Color.White, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                commandesList.forEach { cmd ->
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Table ${cmd.numeroTable} • ${cmd.nombreCouverts} cvts",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (cmd.plats.any { it.contientRavigote || it.nom.contains("tête de veau", true) }) {
                            Text("⚡", color = jauneMenu, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    Divider(color = Color(0x33222222), thickness = 0.7.dp, modifier = Modifier.padding(vertical = 2.dp))
                }
            }
        }
    }
}

// --------- Utilitaire card dashboard style ---------
@Composable
fun DashboardCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF292929)),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            content = content
        )
    }
}
