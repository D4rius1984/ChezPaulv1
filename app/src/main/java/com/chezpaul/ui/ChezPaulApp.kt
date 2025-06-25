package com.chezpaul.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
                                    mainViewModel.commandes.removeAll { it.numeroTable == commandeEnCours!!.numeroTable }
                                    mainViewModel.commandes.add(commandeEnCours!!)
                                    commandeEnCours = null
                                    showResume = false
                                }
                            },
                            onSupprimeTable = { commandeASupprimer ->
                                mainViewModel.commandes.remove(commandeASupprimer)
                            },
                            onModifieTable = { commandeAModifier ->
                                commandeEnCours = commandeAModifier
                                mainViewModel.commandes.remove(commandeAModifier)
                                showResume = false // Reviens à l’écran de commande
                                selectedRoute = "commandes"
                            },
                            onModifieGroupeTable = { /* À compléter pour groupes si besoin */ }
                        )
                    }
                }
                "tables" -> ResumeScreen(
                    commande = null,
                    commandesList = mainViewModel.commandes,
                    onValide = {},
                    onSupprimeTable = { commandeASupprimer ->
                        mainViewModel.commandes.remove(commandeASupprimer)
                    },
                    onModifieTable = { commandeAModifier ->
                        commandeEnCours = commandeAModifier
                        mainViewModel.commandes.remove(commandeAModifier)
                        showResume = false
                        selectedRoute = "commandes"
                    },
                    onModifieGroupeTable = { /* idem, groupes */ }
                )
                // Modif ici : on passe la lambda pour ajouter à la liste des commandes
                "groupes" -> GroupeCommandeScreen(
                    onCommandeValidee = { commandeGroupe ->
                        mainViewModel.commandes.removeAll { it.numeroTable == commandeGroupe.numeroTable }
                        mainViewModel.commandes.add(commandeGroupe)
                        // Optionnel : revenir à ResumeScreen ou Tables après validation
                        // selectedRoute = "tables"
                    }
                )
                "settings" -> SettingsScreen(
                    viewModel = mainViewModel
                )
                "accueil" -> AccueilScreen(commandesList = mainViewModel.commandes)
            }
        }
    }
}
