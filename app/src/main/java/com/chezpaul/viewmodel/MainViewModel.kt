package com.chezpaul.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.chezpaul.model.Commande

class MainViewModel : ViewModel() {
    var commandes = mutableStateListOf<Commande>()
    var commandeEnEdition = mutableStateOf<Commande?>(null)
    var commandeGroupeEnEdition = mutableStateOf<Commande?>(null)

    fun ajouterCommande(cmd: Commande) {
        // Remplace bien la commande à numéro de table identique, que ce soit groupe ou non
        commandes.removeAll { it.numeroTable == cmd.numeroTable }
        commandes.add(cmd)
        // On clean l'état d'édition à chaque ajout
        commandeEnEdition.value = null
        commandeGroupeEnEdition.value = null
    }

    fun supprimerCommande(cmd: Commande) {
        commandes.remove(cmd)
        // Reset l'édition si on supprime une commande qui était en édition
        if (commandeEnEdition.value == cmd) commandeEnEdition.value = null
        if (commandeGroupeEnEdition.value == cmd) commandeGroupeEnEdition.value = null
    }

    fun editerCommande(cmd: Commande) {
        commandeEnEdition.value = cmd
        commandeGroupeEnEdition.value = null // Sécurité, ne jamais éditer les deux en même temps
    }

    fun editerCommandeGroupe(cmd: Commande) {
        commandeGroupeEnEdition.value = cmd
        commandeEnEdition.value = null // Sécurité, ne jamais éditer les deux en même temps
    }

    // Cette fonction réinitialise toutes les commandes (Fin de service)
    fun resetAll() {
        commandes.clear()
        commandeEnEdition.value = null
        commandeGroupeEnEdition.value = null
    }
}
