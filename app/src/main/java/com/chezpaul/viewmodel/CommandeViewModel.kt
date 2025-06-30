package com.chezpaul.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.chezpaul.model.Commande

class CommandeViewModel : ViewModel() {
    private val _commandesList = mutableStateOf<List<Commande>>(emptyList())
    val commandesList = _commandesList

    // Ajouter une nouvelle commande
    fun ajouterCommande(cmd: Commande) {
        // Check if the cmd is being passed correctly and add to the list
        _commandesList.value = _commandesList.value + cmd
    }

    // Modifier une commande existante
    fun modifierCommande(cmd: Commande) {
        // Compare by numeroTable and replace the existing one
        _commandesList.value = _commandesList.value.map { commande ->
            if (commande.numeroTable == cmd.numeroTable) cmd
            else commande
        }
    }

    // Supprimer une commande
    fun supprimerCommande(cmd: Commande) {
        _commandesList.value = _commandesList.value.filterNot { it.numeroTable == cmd.numeroTable }
    }

    // Valider une commande (add your logic here)
    fun validerCommande(cmd: Commande) {
        // Placeholder for the actual validation logic
    }
}
