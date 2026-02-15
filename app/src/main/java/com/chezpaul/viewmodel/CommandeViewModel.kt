package com.chezpaul.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.chezpaul.model.Commande

class CommandeViewModel : ViewModel() {
    private val _commandesList = mutableStateOf<List<Commande>>(emptyList())
    val commandesList = _commandesList

    // Etat pour résultat validation (true = ok, false = erreur)
    val validerCommandeResult = mutableStateOf(true)

    // Constantes pour les plats illimités
    private val nomCervelle = "cervelle"
    private val nomStMarcelin = "st marcelin"

    // Ajouter une nouvelle commande (triée par numéro de table)
    fun ajouterCommande(cmd: Commande) {
        _commandesList.value = (_commandesList.value + cmd).sortedBy { it.numeroTable.toIntOrNull() ?: Int.MAX_VALUE }
    }

    // Modifier une commande existante (triée par numéro de table)
    fun modifierCommande(cmd: Commande) {
        _commandesList.value = _commandesList.value.map { commande ->
            if (commande.numeroTable == cmd.numeroTable) cmd else commande
        }.sortedBy { it.numeroTable.toIntOrNull() ?: Int.MAX_VALUE }
    }

    // Supprimer une commande
    fun supprimerCommande(cmd: Commande) {
        _commandesList.value = _commandesList.value.filterNot { it.numeroTable == cmd.numeroTable }
    }

    // Validation adaptée : permet boissons seules OU plats selon la règle 1 couvert = 1 plat
    fun verifierPlatsSelonCouverts(cmd: Commande): Boolean {
        val nbCouverts = cmd.nombreCouverts
        val aDesPlats = cmd.plats.isNotEmpty()
        val aDesBoissons = cmd.boissons.isNotEmpty()

        // Cas 1: Seulement des boissons = toujours OK
        if (!aDesPlats && aDesBoissons) {
            return true
        }

        // Cas 2: Des plats (avec ou sans boissons) = vérifier la règle 1 couvert = 1 plat
        if (aDesPlats) {
            val totalPlats = cmd.plats
                .filter { it.nom.lowercase() != nomCervelle && it.nom.lowercase() != nomStMarcelin }
                .sumOf { it.quantite }
            return totalPlats == nbCouverts
        }

        // Cas 3: Ni plats ni boissons = pas valide
        return false
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