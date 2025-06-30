package com.chezpaul.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chezpaul.model.BoissonConfig
import com.chezpaul.model.boissonsList

class BoissonViewModel : ViewModel() {

    // Liste des boissons observée
    private val _boissons = MutableLiveData<List<BoissonConfig>>()
    val boissons: LiveData<List<BoissonConfig>> get() = _boissons

    // Etat d'activation pour chaque boisson
    private val _boissonsActivationState = MutableLiveData<Map<String, Boolean>>()
    val boissonsActivationState: LiveData<Map<String, Boolean>> get() = _boissonsActivationState

    init {
        loadBoissons() // Charger les boissons initiales
    }

    private fun loadBoissons() {
        _boissons.value = boissonsList // Liste statique des boissons
        // Initialiser les états d'activation à true pour toutes les boissons par défaut
        _boissonsActivationState.value = boissonsList.associate { it.nom to true }
    }

    // Fonction pour activer/désactiver une boisson - simplifiée
    fun toggleBoissonActivation(boissonNom: String, isActivated: Boolean) {
        val currentState = _boissonsActivationState.value ?: emptyMap()
        _boissonsActivationState.value = currentState.toMutableMap().apply {
            this[boissonNom] = isActivated
        }
    }

    // Fonction pour récupérer l'état d'activation d'une boisson
    fun getBoissonActivationState(boissonNom: String): Boolean {
        return _boissonsActivationState.value?.get(boissonNom) ?: true // Retourne true par défaut
    }

    // Sauvegarder l'état d'activation des boissons
    fun updateBoissonActivation(updatedState: Map<String, Boolean>) {
        _boissonsActivationState.value = updatedState
    }

    // **NOUVELLE MÉTHODE** - Réinitialise toutes les boissons (pour fin de service)
    fun resetAllBoissons() {
        // Remet toutes les boissons à leur état par défaut (activées)
        _boissonsActivationState.value = boissonsList.associate { it.nom to true }
    }
}