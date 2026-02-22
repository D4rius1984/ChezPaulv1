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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chezpaul.model.Commande
import com.chezpaul.model.MenuConstants
import com.chezpaul.ui.components.formatPrix
import com.chezpaul.ui.theme.ChezPaulColors
import com.chezpaul.viewmodel.CommandeViewModel
import com.chezpaul.viewmodel.PrinterViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumeScreen(
    commandeViewModel: CommandeViewModel,
    commande: Commande?,
    onValide: () -> Unit,
    onSupprimeTable: (Commande) -> Unit,
    onModifieTable: (Commande) -> Unit,
    isInCommandeFlow: Boolean,
    printerViewModel: PrinterViewModel? = null
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val haptic = LocalHapticFeedback.current
    val jauneMenu = ChezPaulColors.JauneMenu

    // Observer les données du ViewModel
    val commandesList by commandeViewModel.commandesList
    val showBottomSheet by commandeViewModel.showBottomSheet
    val selectedCommande by commandeViewModel.selectedCommande

    val bottomSheetState = rememberModalBottomSheetState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ChezPaulColors.FondPrincipal)
            .padding(16.dp)
    ) {
        Text(
            "Résumé de la commande",
            style = MaterialTheme.typography.headlineSmall,
            color = ChezPaulColors.JauneMenu,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Commandes en cours : seulement affichées si on est dans le flow commande
        if (isInCommandeFlow) {
            // CARD Commande en cours
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ChezPaulColors.FondCard),
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
                                        color = ChezPaulColors.TexteNoir,
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
                        if (commande.isGroupe && commande.prixMenuGroupe != null) {
                            Text(
                                "Boissons (incluses ${commande.prixMenuGroupe.formatPrix()}/cvt) :",
                                color = Color.White,
                                style = MaterialTheme.typography.titleSmall
                            )
                        } else {
                            Text("Boissons :", color = Color.White, style = MaterialTheme.typography.titleSmall)
                        }
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
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onValide()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = jauneMenu,
                                contentColor = ChezPaulColors.TexteNoir
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
            colors = CardDefaults.cardColors(containerColor = ChezPaulColors.FondCard),
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
                                                        color = ChezPaulColors.TexteNoir,
                                                        fontWeight = FontWeight.Bold,
                                                        style = MaterialTheme.typography.labelSmall,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }
                                            // Badge Enfants
                                            if (cmd.menusEnfants > 0) {
                                                Spacer(Modifier.width(8.dp))
                                                Surface(
                                                    color = ChezPaulColors.OrangeMenu.copy(alpha = 0.2f),
                                                    shape = RoundedCornerShape(4.dp)
                                                ) {
                                                    Text(
                                                        "${cmd.menusEnfants} enfant${if (cmd.menusEnfants > 1) "s" else ""}",
                                                        color = ChezPaulColors.OrangeMenu,
                                                        fontWeight = FontWeight.Bold,
                                                        style = MaterialTheme.typography.labelSmall,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }
                                        }
                                        val prixTotal = remember(cmd) {
                                            if (cmd.isGroupe && cmd.prixMenuGroupe != null) {
                                                cmd.nombreCouverts * cmd.prixMenuGroupe
                                            } else {
                                                val prixPlats = if (cmd.plats.isNotEmpty()) {
                                                    val adultes = cmd.nombreCouverts - cmd.menusEnfants
                                                    adultes * commandeViewModel.menuPrice.value + cmd.menusEnfants * MenuConstants.PRIX_MENU_ENFANT
                                                } else 0.0
                                                val prixBoissons = cmd.boissons.sumOf { it.quantite * it.prix }
                                                prixPlats + prixBoissons
                                            }
                                        }
                                        val ticketMoyen = remember(cmd) { if (cmd.nombreCouverts > 0) prixTotal / cmd.nombreCouverts else 0.0 }
                                        val tmColor = getTicketMoyenColor(ticketMoyen)
                                        Text(
                                            "${prixTotal.formatPrix()} · TM ${ticketMoyen.formatPrix()}",
                                            color = tmColor,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.bodySmall,
                                            maxLines = 1
                                        )
                                        Icon(
                                            imageVector = Icons.Default.MoreVert,
                                            contentDescription = "Options",
                                            tint = Color.White,
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clickable {
                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                    commandeViewModel.toggleBottomSheet(cmd)
                                                }
                                        )
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
                                        val boissonSuffix = if (cmd.isGroupe && cmd.prixMenuGroupe != null) " (incluses)" else ""
                                        Text(
                                            "Boissons$boissonSuffix : " + cmd.boissons.joinToString { "${it.nom} x${it.quantite}" },
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

                                    // Timestamp
                                    val timeFormat = remember { SimpleDateFormat("HH'h'mm", Locale.FRENCH) }
                                    val timeLabel = if (cmd.modifiedAt != null)
                                        "Modifiée à ${timeFormat.format(Date(cmd.modifiedAt))}"
                                    else
                                        "Ajoutée à ${timeFormat.format(Date(cmd.timestamp))}"
                                    Text(
                                        timeLabel,
                                        color = Color.Gray,
                                        style = MaterialTheme.typography.labelSmall
                                    )

                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        thickness = 0.7.dp,
                                        color = ChezPaulColors.DividerColor
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
                commandeViewModel.toggleBottomSheet(null)
            },
            sheetState = bottomSheetState,
            containerColor = ChezPaulColors.FondCard,
            contentColor = Color.White,
            dragHandle = {
                Surface(
                    modifier = Modifier
                        .padding(vertical = 11.dp)
                        .size(width = 32.dp, height = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Gray
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
                    color = ChezPaulColors.DividerColor,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Option Modifier
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedCommande?.let { commande ->
                                commandeViewModel.toggleBottomSheet(null)
                                onModifieTable(commande)
                            }
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

                // Option Imprimer
                if (printerViewModel != null && printerViewModel.isPrinterEnabled.value) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedCommande?.let { commande ->
                                    printerViewModel.printCommande(commande, context)
                                    commandeViewModel.toggleBottomSheet(null)
                                }
                            }
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "\uD83D\uDDA8",
                            modifier = Modifier.size(24.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Imprimer en cuisine",
                            style = MaterialTheme.typography.bodyLarge,
                            color = ChezPaulColors.JauneMenu
                        )
                    }
                }

                // Option Supprimer
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedCommande?.let { commande ->
                                commandeViewModel.deleteCommande(commande)
                                onSupprimeTable(commande)
                            }
                        }
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = ChezPaulColors.RougeErreur,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Supprimer",
                        style = MaterialTheme.typography.bodyLarge,
                        color = ChezPaulColors.RougeErreur
                    )
                }
            }
        }
    }
}

private fun getTicketMoyenColor(ticketMoyen: Double): Color {
    return when {
        ticketMoyen >= 49.0 -> Color(0xFFFF9800) // Orange - ticket élevé
        ticketMoyen >= 40.0 -> ChezPaulColors.JauneMenu // Jaune - bon ticket
        ticketMoyen >= 32.0 -> Color.White       // Blanc - standard (menu)
        else -> Color.Gray                       // Gris - en cours / faible
    }
}