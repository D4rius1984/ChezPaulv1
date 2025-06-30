package com.chezpaul.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.chezpaul.model.Commande

class ResumeViewModel(
    private val commandeViewModel: CommandeViewModel // Injection du CommandeViewModel
) : ViewModel() {

    // Liste des commandes ouvertes
    val commandesList: State<List<Commande>> = commandeViewModel.commandesList

    // Etat du BottomSheet et commande sélectionnée
    private val _showBottomSheet = mutableStateOf(false)
    val showBottomSheet: State<Boolean> = _showBottomSheet

    private val _selectedCommande = mutableStateOf<Commande?>(null)
    val selectedCommande: State<Commande?> = _selectedCommande

    // Toggle de l'état BottomSheet
    fun toggleBottomSheet(commande: Commande?) {
        _selectedCommande.value = commande
        _showBottomSheet.value = !_showBottomSheet.value
    }

    // Actions : Modifier une commande
    fun modifyCommande(commande: Commande) {
        commandeViewModel.modifierCommande(commande)
    }

    // Actions : Supprimer une commande
    fun deleteCommande(commande: Commande) {
        commandeViewModel.supprimerCommande(commande)
    }

    // Valider une commande
    fun validateCommande(commande: Commande) {
        commandeViewModel.validerCommande(commande)
    }
}
