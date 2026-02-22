package com.chezpaul.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.chezpaul.ui.theme.ChezPaulColors
import com.chezpaul.viewmodel.BoissonViewModel
import com.chezpaul.viewmodel.PlatViewModel
import com.chezpaul.model.BoissonConfig
import com.chezpaul.model.PlatConfig
import com.chezpaul.model.CategorieBoisson
import com.chezpaul.model.displayName

@Composable
fun MenuModificationScreen(
    platViewModel: PlatViewModel,
    boissonViewModel: BoissonViewModel,
    onValidate: () -> Unit = {}
) {
    val jauneMenu = ChezPaulColors.JauneMenu
    val orangeMenu = ChezPaulColors.OrangeMenu
    val haptic = LocalHapticFeedback.current

    // Onglets et leur état sélectionné
    var selectedTab by remember { mutableStateOf(0) } // 0 = Plats, 1 = Boissons
    var isGroupePreview by remember { mutableStateOf(false) } // Toggle pour prévisualiser groupe/non-groupe

    // Observer les données et états depuis les ViewModels
    val plats by platViewModel.plats
    val boissons by boissonViewModel.boissons
    val platsActivationState by platViewModel.platsActivationState
    val boissonsActivationState by boissonViewModel.boissonsActivationState

    val context = LocalContext.current

    // Fonction pour afficher la liste avec des cases à cocher pour Plats et Boissons
    @Composable
    fun generateItemList(
        isPlats: Boolean,
        items: List<Any>,
        activationState: Map<String, Boolean>,
        toggleActivation: (String, Boolean) -> Unit,
        isGroupeMode: Boolean
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            if (isPlats) {
                // Pour les plats, filtrer selon le mode groupe/non-groupe
                val platsFiltres = items.map { it as PlatConfig }.filter { plat ->
                    if (isGroupeMode) plat.isGroupe else plat.isNonGroupe
                }

                items(platsFiltres, key = { it.nom }) { plat ->
                    val isActivated = activationState[plat.nom] ?: true

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = plat.nom,
                            modifier = Modifier.weight(1f),
                            color = Color.White
                        )
                        Checkbox(
                            checked = isActivated,
                            onCheckedChange = { checked ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                toggleActivation(plat.nom, checked)
                            },
                            colors = CheckboxDefaults.colors(checkedColor = jauneMenu)
                        )
                    }
                }
            } else {
                // Pour les boissons, filtrer selon le mode ET grouper par catégorie
                val boissonsFiltrees = items.map { it as BoissonConfig }.filter { boisson ->
                    if (isGroupeMode) boisson.isGroupe else boisson.isNonGroupe
                }
                val boissonsByCategory = boissonsFiltrees.groupBy { it.categorie }

                boissonsByCategory.forEach { (categorie, boissonsDeCategorie) ->
                    // Header de catégorie
                    item {
                        Text(
                            text = categorie.displayName,
                            color = jauneMenu,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }

                    // Boissons de cette catégorie
                    items(boissonsDeCategorie, key = { it.nom }) { boisson ->
                        val isActivated = activationState[boisson.nom] ?: true

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = boisson.nom,
                                modifier = Modifier.weight(1f),
                                color = Color.White
                            )
                            Checkbox(
                                checked = isActivated,
                                onCheckedChange = { checked ->
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    toggleActivation(boisson.nom, checked)
                                },
                                colors = CheckboxDefaults.colors(checkedColor = jauneMenu)
                            )
                        }
                    }
                }
            }
        }
    }

    // Affichage des onglets
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ChezPaulColors.FondPrincipal)
            .padding(12.dp)
    ) {
        Text(
            "Modification du Menu",
            style = MaterialTheme.typography.headlineSmall,
            color = jauneMenu,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = jauneMenu,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = jauneMenu
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    selectedTab = 0
                },
                text = { Text("Plats", color = if (selectedTab == 0) jauneMenu else Color.White) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    selectedTab = 1
                },
                text = { Text("Boissons", color = if (selectedTab == 1) jauneMenu else Color.White) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Switch pour prévisualiser le menu groupe/non-groupe
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Prévisualisation menu :",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Non-groupe",
                    color = if (!isGroupePreview) jauneMenu else Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = isGroupePreview,
                    onCheckedChange = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        isGroupePreview = it
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = jauneMenu,
                        checkedTrackColor = jauneMenu.copy(alpha = 0.5f)
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Groupe",
                    color = if (isGroupePreview) jauneMenu else Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Card layout for Plat and Boisson selections
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = ChezPaulColors.FondCard),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(Modifier.padding(16.dp)) {
                // Affichage des éléments selon l'onglet sélectionné
                if (selectedTab == 0) {
                    generateItemList(
                        isPlats = true,
                        items = plats,
                        activationState = platsActivationState,
                        toggleActivation = { platNom, isActivated ->
                            platViewModel.togglePlatActivation(platNom, isActivated)
                        },
                        isGroupeMode = isGroupePreview
                    )
                } else {
                    generateItemList(
                        isPlats = false,
                        items = boissons,
                        activationState = boissonsActivationState,
                        toggleActivation = { boissonNom, isActivated ->
                            boissonViewModel.toggleBoissonActivation(boissonNom, isActivated)
                        },
                        isGroupeMode = isGroupePreview
                    )
                }
            }
        }

        // Button to validate the selection, placed at the bottom
        Button(
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                Toast.makeText(context, "Modifications sauvegardées", Toast.LENGTH_SHORT).show()
                onValidate()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = jauneMenu)
        ) {
            Text("Valider les modifications", fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}