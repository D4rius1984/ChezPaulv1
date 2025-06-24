package com.chezpaul.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chezpaul.model.Commande
import com.chezpaul.ui.screens.*
import com.chezpaul.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChezPaulApp() {
    // 1. Instancie le ViewModel partagé
    val mainViewModel: MainViewModel = viewModel()

    var selectedRoute by remember { mutableStateOf("commandes") }
    var commandeEnCours by remember { mutableStateOf<Commande?>(null) }
    var showResume by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedRoute = selectedRoute,
                onItemSelected = { route ->
                    if (route == "ajouter") {
                        commandeEnCours = null
                        showResume = false
                        selectedRoute = "commandes"
                    } else {
                        selectedRoute = route
                    }
                },
                onAddClick = {
                    commandeEnCours = null
                    showResume = false
                    selectedRoute = "commandes"
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (selectedRoute) {
                "commandes" -> {
                    if (!showResume) {
                        CommandeScreen(
                            commande = commandeEnCours,
                            onNext = { nouvelleCommande ->
                                commandeEnCours = nouvelleCommande
                                showResume = true
                            }
                        )
                    } else {
                        ResumeScreen(
                            commande = commandeEnCours,
                            commandesList = mainViewModel.commandes,
                            onValide = {
                                if (commandeEnCours != null) {
                                    mainViewModel.commandes.add(commandeEnCours!!)
                                    commandeEnCours = null
                                    showResume = false
                                }
                            }
                        )
                    }
                }
                "tables" -> ResumeScreen(
                    commande = null,
                    commandesList = mainViewModel.commandes,
                    onValide = {}
                )
                "groupes" -> GroupesScreen()
                "settings" -> SettingsScreen(
                    viewModel = mainViewModel
                )
                "accueil" -> AccueilScreen(commandesList = mainViewModel.commandes)
            }
        }
    }
}

// ---- Ecran Groupes temporaire (à personnaliser plus tard) ----
@Composable
fun GroupesScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Écran Groupes (à faire)")
    }
}
