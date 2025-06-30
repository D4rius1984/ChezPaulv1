package com.chezpaul.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.chezpaul.model.Commande

class CommandeViewModel : ViewModel() {
    private val _commandesList = mutableStateOf<List<Commande>>(emptyList())
    val commandesList = _commandesList

    // Ajouter une nouvelle commande
    fun ajouterCommande(cmd: Commande) {
        _commandesList.value = _commandesList.value + cmd
    }

    // Modifier une commande existante
    fun modifierCommande(cmd: Commande) {
        _commandesList.value = _commandesList.value.map { commande ->
            if (commande.numeroTable == cmd.numeroTable) cmd else commande
        }
    }

    // Supprimer une commande
    fun supprimerCommande(cmd: Commande) {
        _commandesList.value = _commandesList.value.filterNot { it.numeroTable == cmd.numeroTable }
    }

    // Valider une commande : ajouter à la liste si pas encore présente
    fun validerCommande(cmd: Commande) {
        // Si déjà présente, on la modifie ; sinon, on l'ajoute
        val existe = _commandesList.value.any { it.numeroTable == cmd.numeroTable }
        if (existe) {
            modifierCommande(cmd)
        } else {
            ajouterCommande(cmd)
        }
    }
}
