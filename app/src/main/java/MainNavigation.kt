package com.chezpaul.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chezpaul.ui.screens.*
import com.chezpaul.viewmodel.MainViewModel

@Composable
fun MainNavigation() {
    val vm: MainViewModel = viewModel()
    var selectedRoute by remember { mutableStateOf("accueil") }
    var modifierTableActive by remember { mutableStateOf(false) }
    var modifierGroupeActive by remember { mutableStateOf(false) }

    // --- Bloc édition commande classique ---
    if (modifierTableActive && vm.commandeEnEdition.value != null) {
        CommandeScreen(
            commande = vm.commandeEnEdition.value,
            onNext = {
                vm.ajouterCommande(it)
                modifierTableActive = false // IMPORTANT : d'abord !
                selectedRoute = "tables"
                vm.commandeEnEdition.value = null
            }
        )
        return
    }

    // --- Bloc édition commande groupe ---
    if (modifierGroupeActive && vm.commandeGroupeEnEdition.value != null) {
        GroupeCommandeScreen(
            commande = vm.commandeGroupeEnEdition.value,
            onCommandeValidee = {
                vm.ajouterCommande(it)
                modifierGroupeActive = false // IMPORTANT : d'abord !
                selectedRoute = "tables"
                vm.commandeGroupeEnEdition.value = null
            }
        )
        return
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedRoute = selectedRoute,
                onItemSelected = { route -> selectedRoute = route },
                onAddClick = { selectedRoute = "ajouter" }
            )
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            when (selectedRoute) {
                "accueil" -> AccueilScreen(commandesList = vm.commandes)
                "tables" -> ResumeScreen(
                    commande = null,
                    onValide = {},
                    commandesList = vm.commandes,
                    onSupprimeTable = { vm.supprimerCommande(it) },
                    onModifieTable = { cmd ->
                        vm.editerCommande(cmd)
                        modifierTableActive = true
                        selectedRoute = "ajouter"   // <-- Ajouté ici
                    },
                    onModifieGroupeTable = { cmd ->
                        vm.editerCommandeGroupe(cmd)
                        modifierGroupeActive = true
                        selectedRoute = "groupes"   // <-- Ajouté ici
                    }
                )
                "ajouter" -> CommandeScreen(
                    commande = null,
                    onNext = {
                        vm.ajouterCommande(it)
                        selectedRoute = "tables"
                    }
                )
                "groupes" -> GroupeCommandeScreen(
                    commande = null,
                    onCommandeValidee = {
                        vm.ajouterCommande(it)
                        selectedRoute = "tables"
                    }
                )
                "settings" -> SettingsScreen()
                else -> Text("Écran inconnu")
            }
        }
    }
}
