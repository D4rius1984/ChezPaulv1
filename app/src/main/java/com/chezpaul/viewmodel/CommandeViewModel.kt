package com.chezpaul.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.chezpaul.model.Commande

class CommandeViewModel : ViewModel() {
    private val _commandesList = mutableStateOf<List<Commande>>(emptyList())
    val commandesList = _commandesList

    // Etat pour résultat validation (true = ok, false = erreur)
    val validerCommandeResult = mutableStateOf(true)

    // Constante nom cervelle (pour éviter string "magique")
    private val nomCervelle = "cervelle"

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

    // Validation : 1 couvert = 1 plat sauf cervelle illimitée
    fun verifierPlatsSelonCouverts(cmd: Commande): Boolean {
        val nbCouverts = cmd.nombreCouverts

        // Somme des plats hors cervelle
        val totalPlats = cmd.plats
            .filter { it.nom.lowercase() != nomCervelle }
            .sumOf { it.quantite }

        return totalPlats == nbCouverts
    }

    // Valider une commande : ajoute/modifie seulement si la validation passe
    fun validerCommande(cmd: Commande) {
        val estValide = verifierPlatsSelonCouverts(cmd)
        validerCommandeResult.value = estValide

        if (estValide) {
            val existe = _commandesList.value.any { it.numeroTable == cmd.numeroTable }
            if (existe) {
                modifierCommande(cmd)
            } else {
                ajouterCommande(cmd)
            }
        }
        // Sinon ne fait rien (à l'UI d'afficher un message)
    }

    // **NOUVELLE MÉTHODE** - Réinitialise toutes les commandes (pour fin de service)
    fun resetAllCommandes() {
        _commandesList.value = emptyList()
        validerCommandeResult.value = true
    }
}