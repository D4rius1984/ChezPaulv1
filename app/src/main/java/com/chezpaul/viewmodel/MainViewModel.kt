package com.chezpaul.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.chezpaul.model.Commande

class MainViewModel(
    private val commandeViewModel: CommandeViewModel // Injection du CommandeViewModel
) : ViewModel() {

    var isDarkTheme = mutableStateOf(false)
    var platsSpeciauxState = mutableStateOf(false)
    var ravigoteNotif = mutableStateOf(false)

    fun resetAll() {
        platsSpeciauxState.value = false
        ravigoteNotif.value = false
    }

    // Ajouter une commande
    fun ajouterCommande(cmd: Commande) {
        commandeViewModel.ajouterCommande(cmd)
    }

    // Supprimer une commande
    fun supprimerCommande(cmd: Commande) {
        commandeViewModel.supprimerCommande(cmd)
    }
}
