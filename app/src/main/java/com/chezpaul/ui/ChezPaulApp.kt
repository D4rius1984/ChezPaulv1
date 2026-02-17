package com.chezpaul.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chezpaul.model.Commande
import com.chezpaul.ui.navigation.Screen
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
import com.chezpaul.viewmodel.AccueilViewModel
import com.chezpaul.viewmodel.ResumeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChezPaulApp(viewModel: BottomNavViewModel) {
    // Initialisation des ViewModels principaux
    val commandeViewModel: CommandeViewModel = viewModel()
    val platViewModel: PlatViewModel = viewModel()
    val boissonViewModel: BoissonViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()
    val printerViewModel: PrinterViewModel = viewModel()

    // ViewModels spécialisés qui dépendent du CommandeViewModel
    val accueilViewModel = AccueilViewModel(commandeViewModel)
    val resumeViewModel = ResumeViewModel(commandeViewModel)

    // Observer les états d'activation depuis les ViewModels
    val platsActivationState by platViewModel.platsActivationState.observeAsState(emptyMap())
    val boissonsActivationState by boissonViewModel.boissonsActivationState.observeAsState(emptyMap())

    // State pour la navigation et le flow de commande
    val currentScreen by viewModel.selectedScreen
    var commandeEnCours by remember { mutableStateOf<Commande?>(null) }
    var showResume by remember { mutableStateOf(false) }

    ChezPaulTheme {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    selectedScreen = currentScreen,
                    onItemSelected = { screen ->
                        viewModel.selectScreen(screen)
                    },
                    onAddClick = {
                        commandeEnCours = null
                        showResume = false
                        viewModel.selectScreen(Screen.Commandes)
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                when (currentScreen) {
                    Screen.Accueil -> AccueilScreen(
                        accueilViewModel = accueilViewModel
                    )

                    Screen.Commandes -> {
                        if (!showResume) {
                            CommandeScreen(
                                commande = commandeEnCours,
                                onNext = { nouvelleCommande ->
                                    commandeEnCours = nouvelleCommande
                                    showResume = true
                                },
                                platsActivationState = platsActivationState,
                                boissonsActivationState = boissonsActivationState,
                                printerViewModel = printerViewModel
                            )
                        } else {
                            ResumeScreen(
                                resumeViewModel = resumeViewModel,
                                commande = commandeEnCours,
                                onValide = {
                                    commandeEnCours?.let { commande ->
                                        resumeViewModel.validateCommande(commande)
                                        commandeEnCours = null
                                        showResume = false
                                        viewModel.selectScreen(Screen.Accueil)
                                    }
                                },
                                onSupprimeTable = { commandeASupprimer ->
                                    resumeViewModel.deleteCommande(commandeASupprimer)
                                },
                                onModifieTable = { commandeAModifier ->
                                    resumeViewModel.deleteCommande(commandeAModifier)
                                    commandeEnCours = commandeAModifier
                                    showResume = false
                                    viewModel.selectScreen(Screen.Commandes)
                                },
                                isInCommandeFlow = true
                            )
                        }
                    }

                    Screen.Tables -> ResumeScreen(
                        resumeViewModel = resumeViewModel,
                        commande = null,
                        onValide = {},
                        onSupprimeTable = { commandeASupprimer ->
                            resumeViewModel.deleteCommande(commandeASupprimer)
                        },
                        onModifieTable = { commandeAModifier ->
                            resumeViewModel.deleteCommande(commandeAModifier)
                            commandeEnCours = commandeAModifier
                            showResume = false
                            viewModel.selectScreen(Screen.Commandes)
                        },
                        isInCommandeFlow = false
                    )

                    Screen.Settings -> SettingsScreen(
                        settingsViewModel = settingsViewModel,
                        printerViewModel = printerViewModel,
                        commandeViewModel = commandeViewModel,
                        platViewModel = platViewModel,
                        boissonViewModel = boissonViewModel
                    )

                    Screen.Modifier -> {
                        MenuModificationScreen(
                            platViewModel = platViewModel,
                            boissonViewModel = boissonViewModel,
                            onValidate = {
                                viewModel.selectScreen(Screen.Accueil)
                            }
                        )
                    }
                }
            }
        }
    }
}
