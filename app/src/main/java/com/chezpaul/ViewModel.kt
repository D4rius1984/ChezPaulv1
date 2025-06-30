package com.chezpaul.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.chezpaul.model.Commande

class MainViewModel : ViewModel() {
    // La liste des commandes reste, aucune suppression automatique
    var commandes = mutableStateListOf<Commande>()
    var isDarkTheme = mutableStateOf(false)
    var platsSpeciauxState = mutableStateOf(false)
    var ravigoteNotif = mutableStateOf(false)

    fun resetAll() {
        commandes.clear()
        platsSpeciauxState.value = false
        ravigoteNotif.value = false
        // tu peux reset d'autres states ici si besoin
    }

    // SUPPRESSION manuelle d'une commande/table
    fun supprimerCommande(cmd: Commande) {
        commandes.remove(cmd)
    }

    // Commande sélectionnée pour édition (si besoin)
    var commandeEnEdition = mutableStateOf<Commande?>(null)

    fun editerCommande(cmd: Commande) {
        commandeEnEdition.value = cmd
    }

    // Pour ajouter une nouvelle commande
    fun ajouterCommande(cmd: Commande) {
        commandes.add(cmd)
    }
}
