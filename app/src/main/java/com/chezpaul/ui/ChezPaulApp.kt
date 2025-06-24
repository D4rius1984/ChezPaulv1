package com.chezpaul.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chezpaul.model.*
import com.chezpaul.ui.screens.CommandeScreen
import com.chezpaul.ui.screens.ResumeScreen
import com.chezpaul.ui.screens.BoissonsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChezPaulApp() {
    var currentScreen by remember { mutableStateOf(0) }
    var commande by remember { mutableStateOf<Commande?>(null) }
    var commandesList by remember { mutableStateOf(listOf<Commande>()) }

    val tabTitles = listOf("Plats", "Boissons", "Résumé")
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    commande = null
                    currentScreen = 0
                }
            ) { Icon(Icons.Default.Add, contentDescription = "Nouvelle commande") }
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {
            TabRow(selectedTabIndex = currentScreen) {
                tabTitles.forEachIndexed { idx, title ->
                    Tab(selected = currentScreen == idx, onClick = { currentScreen = idx }, text = { Text(title) })
                }
            }

            when (currentScreen) {
                0 -> CommandeScreen(
                    commande = commande,
                    onNext = { newCommande ->
                        commande = newCommande
                        currentScreen = 1
                    }
                )
                1 -> BoissonsScreen(
                    commande = commande,
                    onNext = { withBoissons ->
                        commande = withBoissons
                        currentScreen = 2
                    }
                )
                2 -> ResumeScreen(
                    commande = commande,
                    onValide = {
                        if (commande != null) {
                            commandesList = commandesList + commande!!
                            commande = null
                            currentScreen = 0
                        }
                    },
                    commandesList = commandesList
                )
            }
        }
    }
}
