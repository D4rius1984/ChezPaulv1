package com.chezpaul.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chezpaul.model.Commande

@Composable
fun ResumeScreen(
    commande: Commande?,
    onValide: () -> Unit,
    commandesList: List<Commande>
) {
    val jauneMenu = Color(0xFFFFE066)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF222222))
            .padding(16.dp)
    ) {
        Text(
            "Résumé de la commande",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // CARD Commande en cours
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF292929)),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 18.dp)
        ) {
            Column(Modifier.padding(18.dp)) {
                if (commande != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Table : ${commande.numeroTable}",
                            color = jauneMenu,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.width(16.dp))
                        Text(
                            "Couverts : ${commande.nombreCouverts}",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Spacer(Modifier.height(10.dp))

                    Text("Plats :", color = Color.White, style = MaterialTheme.typography.titleSmall)
                    if (commande.plats.isNotEmpty()) {
                        commande.plats.forEach { plat ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("- ${plat.nom} x${plat.quantite}", color = Color.White)
                                if (plat.contientRavigote) {
                                    Text(" ⚡ Ravigote", color = jauneMenu)
                                }
                            }
                        }
                    } else {
                        Text("Aucun plat", color = Color.Gray)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Boissons :", color = Color.White, style = MaterialTheme.typography.titleSmall)
                    if (commande.boissons.isNotEmpty()) {
                        commande.boissons.forEach { boisson ->
                            Text(
                                "- ${boisson.nom} x${boisson.quantite} (${boisson.categorie.name.lowercase().replaceFirstChar { it.uppercase() }})",
                                color = Color.White
                            )
                        }
                    } else {
                        Text("Aucune boisson", color = Color.Gray)
                    }
                    Spacer(Modifier.height(18.dp))
                    Button(
                        onClick = onValide,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = jauneMenu,
                            contentColor = Color(0xFF222222)
                        )
                    ) {
                        Text("Valider la commande", fontWeight = FontWeight.Bold)
                    }
                } else {
                    Text("Aucune commande en cours", color = Color.Gray)
                }
            }
        }

        // TITRE Commandes ouvertes
        Text(
            "Commandes ouvertes :",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        // CARD liste commandes
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF292929)),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        ) {
            Column(Modifier.padding(18.dp)) {
                if (commandesList.isEmpty()) {
                    Text("Aucune commande ouverte.", color = Color.Gray)
                } else {
                    LazyColumn {
                        items(commandesList) { cmd ->
                            Column(
                                Modifier.padding(vertical = 4.dp)
                            ) {
                                Text(
                                    "Table ${cmd.numeroTable} - ${cmd.nombreCouverts} couverts",
                                    color = jauneMenu,
                                    fontWeight = FontWeight.SemiBold
                                )
                                if (cmd.plats.isNotEmpty()) {
                                    Text(
                                        "Plats : " + cmd.plats.joinToString { "${it.nom} x${it.quantite}" },
                                        color = Color.White
                                    )
                                    if (cmd.plats.any { it.contientRavigote }) {
                                        Text("⚡ Ravigote", color = jauneMenu)
                                    }
                                } else {
                                    Text("Aucun plat", color = Color.Gray)
                                }
                                if (cmd.boissons.isNotEmpty()) {
                                    Text(
                                        "Boissons : " + cmd.boissons.joinToString { "${it.nom} x${it.quantite}" },
                                        color = Color.White
                                    )
                                }
                                Divider(
                                    color = Color(0x33FFFFFF),
                                    thickness = 0.7.dp,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
