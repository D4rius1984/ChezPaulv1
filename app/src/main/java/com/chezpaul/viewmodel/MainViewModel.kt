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
        commandes.removeAll { it.numeroTable == cmd.numeroTable }
        commandes.add(cmd)
    }

    fun supprimerCommande(cmd: Commande) {
        commandes.remove(cmd)
    }

    fun editerCommande(cmd: Commande) {
        commandeEnEdition.value = cmd
    }

    fun editerCommandeGroupe(cmd: Commande) {
        commandeGroupeEnEdition.value = cmd
    }

    // Cette fonction réinitialise toutes les commandes (Fin de service)
    fun resetAll() {
        commandes.clear()
        commandeEnEdition.value = null
        commandeGroupeEnEdition.value = null
    }
}
