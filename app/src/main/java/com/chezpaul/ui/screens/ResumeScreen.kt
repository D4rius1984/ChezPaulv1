package com.chezpaul.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chezpaul.model.Commande

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumeScreen(
    commande: Commande?,
    onValide: () -> Unit,
    commandesList: List<Commande>,
    onSupprimeTable: (Commande) -> Unit,
    onModifieTable: (Commande) -> Unit,
    onModifieGroupeTable: (Commande) -> Unit
) {
    val jauneMenu = Color(0xFFFFE066)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF23190e))
            .padding(16.dp)
    ) {
        Text(
            "Résumé de la commande",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = jauneMenu,
            modifier = Modifier.padding(start = 24.dp, top = 36.dp, bottom = 12.dp)
        )
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
                        if (commande.isGroupe == true) {
                            Surface(
                                color = jauneMenu,
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text(
                                    "GROUPE",
                                    color = Color(0xFF222222),
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            buildString {
                                append("Table : ${commande.numeroTable}")
                                if (commande.isGroupe == true && commande.nomGroupe != null) {
                                    append(" (${commande.nomGroupe})")
                                }
                            },
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
        Text(
            "Commandes ouvertes :",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            modifier = Modifier.padding(bottom = 6.dp)
        )
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
                    val sortedCommandes = commandesList.sortedBy {
                        it.numeroTable.toIntOrNull() ?: Int.MAX_VALUE
                    }
                    LazyColumn {
                        items(sortedCommandes, key = { it.numeroTable }) { cmd ->
                            var expanded by remember { mutableStateOf(false) }
                            Box(Modifier.fillMaxWidth()) {
                                Column(
                                    Modifier
                                        .padding(vertical = 4.dp)
                                        .fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        if (cmd.isGroupe == true) {
                                            Surface(
                                                color = jauneMenu,
                                                shape = RoundedCornerShape(6.dp),
                                                modifier = Modifier.padding(end = 8.dp)
                                            ) {
                                                Text(
                                                    "GROUPE",
                                                    color = Color(0xFF222222),
                                                    fontWeight = FontWeight.Black,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        Text(
                                            buildString {
                                                append("Table ${cmd.numeroTable} - ${cmd.nombreCouverts} couverts")
                                                if (cmd.isGroupe == true && cmd.nomGroupe != null) {
                                                    append(" (${cmd.nomGroupe})")
                                                }
                                            },
                                            color = jauneMenu,
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier.weight(1f)
                                        )
                                        IconButton(onClick = { expanded = true }) {
                                            Icon(
                                                imageVector = Icons.Default.MoreVert,
                                                contentDescription = "Options"
                                            )
                                        }
                                        DropdownMenu(
                                            expanded = expanded,
                                            onDismissRequest = { expanded = false }
                                        ) {
                                            DropdownMenuItem(
                                                text = { Text("Modifier") },
                                                onClick = {
                                                    expanded = false
                                                    if (cmd.isGroupe == true) {
                                                        onModifieGroupeTable(cmd)
                                                    } else {
                                                        onModifieTable(cmd)
                                                    }
                                                },
                                                leadingIcon = {
                                                    Icon(
                                                        Icons.Default.Build,
                                                        contentDescription = null
                                                    )
                                                }
                                            )
                                            DropdownMenuItem(
                                                text = { Text("Supprimer") },
                                                onClick = {
                                                    expanded = false
                                                    onSupprimeTable(cmd)
                                                },
                                                leadingIcon = {
                                                    Icon(
                                                        Icons.Default.Delete,
                                                        contentDescription = null
                                                    )
                                                }
                                            )
                                        }
                                    }
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
}
