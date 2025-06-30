package com.chezpaul.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chezpaul.model.Commande
import com.chezpaul.ui.screens.*
import com.chezpaul.viewmodel.CommandeViewModel
import com.chezpaul.viewmodel.BottomNavViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChezPaulApp(viewModel: BottomNavViewModel) {
    // Initialisation de commandeViewModel via viewModel() pour obtenir le ViewModel
    val commandeViewModel: CommandeViewModel = viewModel()

    // Définir "accueil" comme route par défaut
    var selectedRoute by remember { mutableStateOf(viewModel.selectedRoute.value) }
    var commandeEnCours by remember { mutableStateOf<Commande?>(null) }
    var showResume by remember { mutableStateOf(false) }

    // Observer la route sélectionnée via le ViewModel
    selectedRoute = viewModel.selectedRoute.value

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedRoute = selectedRoute,
                onItemSelected = { route ->
                    if (route == "ajouter") {
                        // Quand on clique sur +, on va à commandes
                        commandeEnCours = null
                        showResume = false
                        viewModel.selectRoute("commandes")
                    } else {
                        viewModel.selectRoute(route)
                    }
                },
                onAddClick = {
                    // Quand on clique sur le bouton +
                    commandeEnCours = null
                    showResume = false
                    viewModel.selectRoute("commandes")
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
                "accueil" -> AccueilScreen(commandesList = commandeViewModel.commandesList.value)  // Utilisation de .value ici

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
                            commandesList = commandeViewModel.commandesList.value,  // Utilisation de .value ici
                            onValide = {
                                if (commandeEnCours != null) {
                                    // Si c'est une modif, retire l'ancienne commande pour éviter les doublons
                                    commandeViewModel.commandesList.value = commandeViewModel.commandesList.value.filter { commande -> commande.numeroTable != commandeEnCours!!.numeroTable }
                                    commandeViewModel.commandesList.value = commandeViewModel.commandesList.value + commandeEnCours!!
                                    commandeEnCours = null
                                    showResume = false
                                    // Retour à l'accueil après validation
                                    viewModel.selectRoute("accueil")
                                }
                            },
                            onSupprimeTable = { commandeASupprimer ->
                                // Supprimer la commande de la liste
                                commandeViewModel.commandesList.value = commandeViewModel.commandesList.value.filter { it != commandeASupprimer }
                            },
                            onModifieTable = { commandeAModifier ->
                                // Relancer le flow de commande en conservant la commande existante
                                commandeEnCours = commandeAModifier
                                showResume = false // Reviens à l'écran de commande
                                viewModel.selectRoute("commandes")
                            },
                            isInCommandeFlow = true // Passer en mode "commande" si nécessaire
                        )
                    }
                }

                "tables" -> ResumeScreen(
                    commande = null,
                    commandesList = commandeViewModel.commandesList.value,  // Utilisation de .value ici
                    onValide = {},
                    onSupprimeTable = { commandeASupprimer ->
                        // Supprimer la commande de la liste
                        commandeViewModel.commandesList.value = commandeViewModel.commandesList.value.filter { it != commandeASupprimer }
                    },
                    onModifieTable = { commandeAModifier ->
                        // Lorsque l'on modifie, on charge la commande en cours
                        commandeEnCours = commandeAModifier
                        showResume = false
                        viewModel.selectRoute("commandes")
                    },
                    isInCommandeFlow = false // Dans "tables", on n'est pas dans le flow de commande
                )

                "groupes" -> GroupesScreen()

                "settings" -> SettingsScreen(viewModel = commandeViewModel)
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
