package com.chezpaul.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class BottomNavViewModel : ViewModel() {
    // Etat de la route sélectionnée
    val selectedRoute = mutableStateOf("accueil")

    // Fonction pour mettre à jour la route sélectionnée
    fun selectRoute(route: String) {
        selectedRoute.value = route
    }
}
