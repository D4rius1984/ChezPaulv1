package com.chezpaul.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.chezpaul.model.Commande

class ResumeViewModel(
    private val commandeViewModel: CommandeViewModel // Injection du CommandeViewModel
) : ViewModel() {

    // Liste des commandes ouvertes
    val commandesList: State<List<Commande>> = commandeViewModel.commandesList

    // État du BottomSheet et commande sélectionnée
    private val _showBottomSheet = mutableStateOf(false)
    val showBottomSheet: State<Boolean> = _showBottomSheet

    private val _selectedCommande = mutableStateOf<Commande?>(null)
    val selectedCommande: State<Commande?> = _selectedCommande

    // Toggle de l'état BottomSheet - MAINTENANT UTILISÉ
    fun toggleBottomSheet(commande: Commande?) {
        _selectedCommande.value = commande
        _showBottomSheet.value = commande != null
    }

    // Actions : Modifier une commande - MAINTENANT UTILISÉ
    fun modifyCommande(commande: Commande) {
        commandeViewModel.modifierCommande(commande)
        _showBottomSheet.value = false
        _selectedCommande.value = null
    }

    // Actions : Supprimer une commande - MAINTENANT UTILISÉ
    fun deleteCommande(commande: Commande) {
        commandeViewModel.supprimerCommande(commande)
        _showBottomSheet.value = false
        _selectedCommande.value = null
    }

    // Valider une commande - MAINTENANT UTILISÉ
    fun validateCommande(commande: Commande) {
        commandeViewModel.validerCommande(commande)
    }
}