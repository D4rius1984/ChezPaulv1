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

    // Définir "accueil" comme route par défaut
    var selectedRoute by remember { mutableStateOf("accueil") }
    var commandeEnCours by remember { mutableStateOf<Commande?>(null) }
    var showResume by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedRoute = selectedRoute,
                onItemSelected = { route ->
                    if (route == "ajouter") {
                        // Quand on clique sur +, on va à commandes
                        commandeEnCours = null
                        showResume = false
                        selectedRoute = "commandes"
                    } else {
                        selectedRoute = route
                    }
                },
                onAddClick = {
                    // Quand on clique sur le bouton +
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
                "accueil" -> AccueilScreen(commandesList = mainViewModel.commandes)

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
                                    // Si c'est une modif, retire l'ancienne commande pour éviter les doublons
                                    mainViewModel.commandes.removeAll { it.numeroTable == commandeEnCours!!.numeroTable }
                                    mainViewModel.commandes.add(commandeEnCours!!)
                                    commandeEnCours = null
                                    showResume = false
                                    // Retour à l'accueil après validation
                                    selectedRoute = "accueil"
                                }
                            },
                            onSupprimeTable = { commandeASupprimer ->
                                // Supprimer la commande de la liste
                                mainViewModel.commandes.remove(commandeASupprimer)
                            },
                            onModifieTable = { commandeAModifier ->
                                // Relancer le flow de commande en conservant la commande existante
                                commandeEnCours = commandeAModifier
                                showResume = false // Reviens à l'écran de commande
                                selectedRoute = "commandes"
                            },
                            isInCommandeFlow = true // Passer en mode "commande" si nécessaire
                        )
                    }
                }

                "tables" -> ResumeScreen(
                    commande = null,
                    commandesList = mainViewModel.commandes,
                    onValide = {},
                    onSupprimeTable = { commandeASupprimer ->
                        // Supprimer la commande de la liste
                        mainViewModel.commandes.remove(commandeASupprimer)
                    },
                    onModifieTable = { commandeAModifier ->
                        // Lorsque l'on modifie, on charge la commande en cours
                        commandeEnCours = commandeAModifier
                        showResume = false
                        selectedRoute = "commandes"
                    },
                    isInCommandeFlow = false // Dans "tables", on n'est pas dans le flow de commande
                )

                "groupes" -> GroupesScreen()

                "settings" -> SettingsScreen(viewModel = mainViewModel)
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