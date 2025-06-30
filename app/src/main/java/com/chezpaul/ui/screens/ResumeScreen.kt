package com.chezpaul.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chezpaul.model.Commande

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumeScreen(
    commande: Commande?,
    onValide: () -> Unit,
    commandesList: List<Commande>,
    onSupprimeTable: (Commande) -> Unit,
    onModifieTable: (Commande) -> Unit,
    isInCommandeFlow: Boolean // Nouveau paramètre pour gérer le flow
) {
    val jauneMenu = Color(0xFFFFE066)
    val bottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedCommande by remember { mutableStateOf<Commande?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF23190e))
            .padding(16.dp)
    ) {
        Text(
            "Résumé de la commande",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Commandes en cours : seulement affichées si on est dans le flow commande
        if (isInCommandeFlow) {
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
                            // Badge Groupe si nécessaire
                            if (commande.isGroupe == true) {
                                Spacer(Modifier.width(12.dp))
                                Surface(
                                    color = jauneMenu,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        "Groupe",
                                        color = Color(0xFF222222),
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
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

                        // Affichage des remarques
                        if (!commande.remarque.isNullOrBlank()) {
                            Spacer(Modifier.height(8.dp))
                            Text("Remarque :", color = Color.White, style = MaterialTheme.typography.titleSmall)
                            Text(commande.remarque, color = Color.White)
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
                        items(commandesList, key = { it.numeroTable }) { cmd ->
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
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                "Table ${cmd.numeroTable} - ${cmd.nombreCouverts} couverts",
                                                color = jauneMenu,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            // Badge Groupe si nécessaire
                                            if (cmd.isGroupe == true) {
                                                Spacer(Modifier.width(8.dp))
                                                Surface(
                                                    color = jauneMenu,
                                                    shape = RoundedCornerShape(4.dp)
                                                ) {
                                                    Text(
                                                        "Groupe",
                                                        color = Color(0xFF222222),
                                                        fontWeight = FontWeight.Bold,
                                                        style = MaterialTheme.typography.labelSmall,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }
                                        }
                                        IconButton(onClick = {
                                            selectedCommande = cmd
                                            showBottomSheet = true
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.MoreVert,
                                                contentDescription = "Options",
                                                tint = Color.White
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
                                        Text("Plats : Aucun plat", color = Color.Gray)
                                    }
                                    if (cmd.boissons.isNotEmpty()) {
                                        Text(
                                            "Boissons : " + cmd.boissons.joinToString { "${it.nom} x${it.quantite}" },
                                            color = Color.White
                                        )
                                    } else {
                                        Text("Boissons : Aucune boisson", color = Color.Gray)
                                    }

                                    // Affichage des remarques dans la liste
                                    if (!cmd.remarque.isNullOrBlank()) {
                                        Text(
                                            "Remarque : ${cmd.remarque}",
                                            color = Color.White.copy(alpha = 0.8f),
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }

                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        thickness = 0.7.dp,
                                        color = Color(0x33FFFFFF)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Bottom Sheet Modal
    if (showBottomSheet && selectedCommande != null) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
                selectedCommande = null
            },
            sheetState = bottomSheetState,
            containerColor = Color(0xFF292929),
            contentColor = Color.White,
            dragHandle = {
                Surface(
                    modifier = Modifier
                        .padding(vertical = 11.dp)
                        .size(width = 32.dp, height = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF666666)
                ) {}
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                // Titre de la commande
                Text(
                    text = "Table ${selectedCommande!!.numeroTable}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = jauneMenu,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )

                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = Color(0x33FFFFFF),
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Option Modifier
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showBottomSheet = false
                            onModifieTable(selectedCommande!!)
                            selectedCommande = null
                        }
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Modifier",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                }

                // Option Supprimer
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showBottomSheet = false
                            onSupprimeTable(selectedCommande!!)
                            selectedCommande = null
                        }
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color(0xFFFF6B6B),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Supprimer",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFFFF6B6B)
                    )
                }
            }
        }
    }
}