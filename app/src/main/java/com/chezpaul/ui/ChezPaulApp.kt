package com.chezpaul.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chezpaul.model.Commande
import com.chezpaul.ui.screens.*
import com.chezpaul.ui.screens.BottomNavigationBar
import com.chezpaul.ui.components.ChezPaulScreen
import com.chezpaul.ui.theme.ChezPaulColors
import com.chezpaul.ui.theme.ChezPaulTheme
import com.chezpaul.ui.screens.MenuModificationScreen
import com.chezpaul.viewmodel.CommandeViewModel
import com.chezpaul.viewmodel.BottomNavViewModel
import com.chezpaul.viewmodel.BoissonViewModel
import com.chezpaul.viewmodel.PlatViewModel
import com.chezpaul.viewmodel.SettingsViewModel
import com.chezpaul.viewmodel.PrinterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChezPaulApp(viewModel: BottomNavViewModel) {
    // Initialisation des ViewModels
    val commandeViewModel: CommandeViewModel = viewModel()
    val platViewModel: PlatViewModel = viewModel()
    val boissonViewModel: BoissonViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()
    val printerViewModel: PrinterViewModel = viewModel()

    // Observer les états d'activation depuis les ViewModels
    val platsActivationState by platViewModel.platsActivationState.observeAsState(emptyMap())
    val boissonsActivationState by boissonViewModel.boissonsActivationState.observeAsState(emptyMap())

    // Définir "accueil" comme route par défaut
    var selectedRoute by remember { mutableStateOf(viewModel.selectedRoute.value) }
    var commandeEnCours by remember { mutableStateOf<Commande?>(null) }
    var showResume by remember { mutableStateOf(false) }

    // Observer la route sélectionnée via le ViewModel
    selectedRoute = viewModel.selectedRoute.value

    ChezPaulTheme {
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
                    "accueil" -> AccueilScreen(commandesList = commandeViewModel.commandesList.value)

                    "commandes" -> {
                        if (!showResume) {
                            CommandeScreen(
                                commande = commandeEnCours,
                                onNext = { nouvelleCommande ->
                                    commandeEnCours = nouvelleCommande
                                    showResume = true
                                },
                                platsActivationState = platsActivationState,
                                boissonsActivationState = boissonsActivationState
                            )
                        } else {
                            ResumeScreen(
                                commande = commandeEnCours,
                                commandesList = commandeViewModel.commandesList.value,
                                onValide = {
                                    if (commandeEnCours != null) {
                                        // Si c'est une modif, retire l'ancienne commande pour éviter les doublons
                                        commandeViewModel.commandesList.value = commandeViewModel.commandesList.value.filter { commande ->
                                            commande.numeroTable != commandeEnCours!!.numeroTable
                                        }
                                        commandeViewModel.commandesList.value = commandeViewModel.commandesList.value + commandeEnCours!!
                                        commandeEnCours = null
                                        showResume = false
                                        // Retour à l'accueil après validation
                                        viewModel.selectRoute("accueil")
                                    }
                                },
                                onSupprimeTable = { commandeASupprimer ->
                                    // Supprimer la commande de la liste
                                    commandeViewModel.commandesList.value = commandeViewModel.commandesList.value.filter {
                                        it != commandeASupprimer
                                    }
                                },
                                onModifieTable = { commandeAModifier ->
                                    // Relancer le flow de commande en conservant la commande existante
                                    commandeEnCours = commandeAModifier
                                    showResume = false // Reviens à l'écran de commande
                                    viewModel.selectRoute("commandes")
                                },
                                isInCommandeFlow = true
                            )
                        }
                    }

                    "tables" -> ResumeScreen(
                        commande = null,
                        commandesList = commandeViewModel.commandesList.value,
                        onValide = {},
                        onSupprimeTable = { commandeASupprimer ->
                            // Supprimer la commande de la liste
                            commandeViewModel.commandesList.value = commandeViewModel.commandesList.value.filter {
                                it != commandeASupprimer
                            }
                        },
                        onModifieTable = { commandeAModifier ->
                            // Lorsque l'on modifie, on charge la commande en cours
                            commandeEnCours = commandeAModifier
                            showResume = false
                            viewModel.selectRoute("commandes")
                        },
                        isInCommandeFlow = false
                    )

                    "groupes" -> GroupesScreen()

                    "settings" -> SettingsScreen(
                        settingsViewModel = settingsViewModel,
                        printerViewModel = printerViewModel,
                        commandeViewModel = commandeViewModel,
                        platViewModel = platViewModel,
                        boissonViewModel = boissonViewModel
                    )

                    "modifier" -> {
                        MenuModificationScreen(
                            platViewModel = platViewModel,
                            boissonViewModel = boissonViewModel,
                            onValidate = {
                                viewModel.selectRoute("accueil")
                            }
                        )
                    }
                }
            }
        }
    }
}

// ---- Ecran Groupes temporaire (à personnaliser plus tard) ----
@Composable
fun GroupesScreen() {
    ChezPaulScreen(title = "Groupes") {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Écran Groupes (à faire)", color = ChezPaulColors.TexteBlanc)
        }
    }
}