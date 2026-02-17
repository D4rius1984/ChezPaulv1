package com.chezpaul.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chezpaul.model.Commande
import com.chezpaul.ui.navigation.Screen
import com.chezpaul.ui.screens.*
import com.chezpaul.ui.theme.ChezPaulTheme
import com.chezpaul.viewmodel.BoissonViewModel
import com.chezpaul.viewmodel.BottomNavViewModel
import com.chezpaul.viewmodel.CommandeViewModel
import com.chezpaul.viewmodel.PlatViewModel
import com.chezpaul.viewmodel.PrinterViewModel
import com.chezpaul.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChezPaulApp(viewModel: BottomNavViewModel) {
    // Initialisation des ViewModels (gérés par le Lifecycle Android)
    val commandeViewModel: CommandeViewModel = viewModel()
    val platViewModel: PlatViewModel = viewModel()
    val boissonViewModel: BoissonViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()
    val printerViewModel: PrinterViewModel = viewModel()

    // Observer les états d'activation depuis les ViewModels
    val platsActivationState by platViewModel.platsActivationState.observeAsState(emptyMap())
    val boissonsActivationState by boissonViewModel.boissonsActivationState.observeAsState(emptyMap())

    // Synchroniser le prix du menu depuis SettingsViewModel vers CommandeViewModel
    val menuModeOverride by settingsViewModel.menuModeOverride
    LaunchedEffect(menuModeOverride) {
        commandeViewModel.menuPrice.value = settingsViewModel.getMenuPrice()
    }

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
                    },
                )
            },
        ) { innerPadding ->
            Box(
                modifier =
                    Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
            ) {
                AppNavigationContent(
                    currentScreen = currentScreen,
                    showResume = showResume,
                    commandeEnCours = commandeEnCours,
                    commandeViewModel = commandeViewModel,
                    settingsViewModel = settingsViewModel,
                    printerViewModel = printerViewModel,
                    platViewModel = platViewModel,
                    boissonViewModel = boissonViewModel,
                    platsActivationState = platsActivationState,
                    boissonsActivationState = boissonsActivationState,
                    onNext = { nouvelleCommande ->
                        commandeEnCours = nouvelleCommande
                        showResume = true
                    },
                    onValide = {
                        commandeEnCours?.let { commande ->
                            commandeViewModel.validateCommande(commande)
                            commandeEnCours = null
                            showResume = false
                            viewModel.selectScreen(Screen.Accueil)
                        }
                    },
                    onSupprimeTable = { commandeASupprimer ->
                        commandeViewModel.deleteCommande(commandeASupprimer)
                    },
                    onModifieTable = { commandeAModifier ->
                        commandeViewModel.startModification(commandeAModifier) { cmd ->
                            commandeEnCours = cmd
                            showResume = false
                            viewModel.selectScreen(Screen.Commandes)
                        }
                    },
                    onNavigate = { screen ->
                        viewModel.selectScreen(screen)
                    },
                    printerViewModelForResume = printerViewModel,
                )
            }
        }
    }
}

@Composable
private fun AppNavigationContent(
    currentScreen: Screen,
    showResume: Boolean,
    commandeEnCours: Commande?,
    commandeViewModel: CommandeViewModel,
    settingsViewModel: SettingsViewModel,
    printerViewModel: PrinterViewModel,
    platViewModel: PlatViewModel,
    boissonViewModel: BoissonViewModel,
    platsActivationState: Map<String, Boolean>,
    boissonsActivationState: Map<String, Boolean>,
    onNext: (Commande) -> Unit,
    onValide: () -> Unit,
    onSupprimeTable: (Commande) -> Unit,
    onModifieTable: (Commande) -> Unit,
    onNavigate: (Screen) -> Unit,
    printerViewModelForResume: PrinterViewModel,
) {
    when (currentScreen) {
        Screen.Accueil ->
            AccueilScreen(
                commandeViewModel = commandeViewModel,
                menuPrice = commandeViewModel.menuPrice.value,
            )

        Screen.Commandes -> {
            if (!showResume) {
                CommandeScreen(
                    commande = commandeEnCours,
                    onNext = onNext,
                    platsActivationState = platsActivationState,
                    boissonsActivationState = boissonsActivationState,
                    printerViewModel = printerViewModel,
                )
            } else {
                ResumeScreen(
                    commandeViewModel = commandeViewModel,
                    commande = commandeEnCours,
                    onValide = onValide,
                    onSupprimeTable = onSupprimeTable,
                    onModifieTable = onModifieTable,
                    isInCommandeFlow = true,
                    printerViewModel = printerViewModelForResume,
                )
            }
        }

        Screen.Tables ->
            ResumeScreen(
                commandeViewModel = commandeViewModel,
                commande = null,
                onValide = {},
                onSupprimeTable = onSupprimeTable,
                onModifieTable = onModifieTable,
                isInCommandeFlow = false,
                printerViewModel = printerViewModelForResume,
            )

        Screen.Settings ->
            SettingsScreen(
                settingsViewModel = settingsViewModel,
                printerViewModel = printerViewModel,
                commandeViewModel = commandeViewModel,
                platViewModel = platViewModel,
                boissonViewModel = boissonViewModel,
                onHistoryClick = { onNavigate(Screen.History) },
            )

        Screen.History ->
            HistoryScreen(
                commandeViewModel = commandeViewModel,
                onBack = { onNavigate(Screen.Settings) },
                onServiceClick = { serviceId -> onNavigate(Screen.ServiceDetail(serviceId)) },
                onStatsClick = { onNavigate(Screen.Stats) },
            )

        is Screen.ServiceDetail ->
            ServiceDetailScreen(
                serviceId = (currentScreen as Screen.ServiceDetail).serviceId,
                commandeViewModel = commandeViewModel,
                onBack = { onNavigate(Screen.History) },
            )

        Screen.Stats ->
            StatsScreen(
                commandeViewModel = commandeViewModel,
                onBack = { onNavigate(Screen.History) },
            )

        Screen.Modifier ->
            MenuModificationScreen(
                platViewModel = platViewModel,
                boissonViewModel = boissonViewModel,
                onValidate = { onNavigate(Screen.Accueil) },
            )
    }
}
