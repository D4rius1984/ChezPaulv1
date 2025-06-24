package com.chezpaul.ui

import com.chezpaul.ui.screens.AccueilScreen
import com.chezpaul.ui.screens.FermerServiceScreen
import com.chezpaul.ui.screens.OptionsScreen
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chezpaul.model.*
import com.chezpaul.ui.screens.CommandeScreen
import com.chezpaul.ui.screens.ResumeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChezPaulApp() {
    var selectedRoute by remember { mutableStateOf("commande") }

    // Commande en cours d'édition
    var commandeEnCours by remember { mutableStateOf<Commande?>(null) }
    // True = on est dans l'écran résumé (récap avant ajout à la liste)
    var showResume by remember { mutableStateOf(false) }
    // Liste des commandes validées
    var commandesList by remember { mutableStateOf(listOf<Commande>()) }

    Scaffold(
        bottomBar = {
            CustomBottomBar(
                selectedRoute = selectedRoute,
                onItemSelected = { route ->
                    // Réinitialise tout si fin de service
                    if (route == "fermer_service") {
                        commandesList = listOf()
                        commandeEnCours = null
                        showResume = false
                    }
                    // Le bouton "+" sert à créer une nouvelle commande vide
                    if (route == "commande") {
                        commandeEnCours = null
                        showResume = false
                    }
                    selectedRoute = route
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
                "accueil" -> AccueilScreen(commandesList = commandesList)
                "tables" -> ResumeScreen(
                    commande = null, // on n'affiche PAS une commande unique ici
                    commandesList = commandesList,
                    onValide = {}    // pas de validation ici
                )
                "commande" -> {
                    if (!showResume) {
                        CommandeScreen(
                            commande = commandeEnCours,
                            onNext = { nouvelleCommande ->
                                commandeEnCours = nouvelleCommande
                                showResume = true // Passe à l'écran résumé
                            }
                        )
                    } else {
                        ResumeScreen(
                            commande = commandeEnCours,
                            commandesList = commandesList,
                            onValide = {
                                if (commandeEnCours != null) {
                                    commandesList = commandesList + commandeEnCours!!
                                    commandeEnCours = null
                                    showResume = false
                                }
                            }
                        )
                    }
                }
                "fermer_service" -> FermerServiceScreen()
                "options" -> OptionsScreen()
            }
        }
    }
}

// ---- Barre de navigation personnalisée ----

@Composable
fun CustomBottomBar(
    selectedRoute: String,
    onItemSelected: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        NavigationBarItem(
            selected = selectedRoute == "accueil",
            onClick = { onItemSelected("accueil") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Accueil") },
            label = { Text("Accueil") }
        )
        NavigationBarItem(
            selected = selectedRoute == "tables",
            onClick = { onItemSelected("tables") },
            icon = { Icon(Icons.Default.List, contentDescription = "Tables") },
            label = { Text("Tables") }
        )

        // Bouton central (+) : reset la commande en cours et lance la saisie d'une nouvelle
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton(
                onClick = { onItemSelected("commande") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 2.dp, pressedElevation = 6.dp),
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nouvelle commande", modifier = Modifier.size(32.dp))
            }
        }

        NavigationBarItem(
            selected = selectedRoute == "fermer_service",
            onClick = { onItemSelected("fermer_service") },
            icon = { Icon(Icons.Default.Stop, contentDescription = "Fermer Service") },
            label = { Text("Fin Service") }
        )
        NavigationBarItem(
            selected = selectedRoute == "options",
            onClick = { onItemSelected("options") },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Options") },
            label = { Text("Options") }
        )
    }
}
