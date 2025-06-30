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
    private val _boissonsActivationState = MutableLiveData<Map<String, Boolean>>(emptyMap())
    val boissonsActivationState: LiveData<Map<String, Boolean>> get() = _boissonsActivationState

    init {
        loadBoissons() // Charger les boissons initiales
    }

    private fun loadBoissons() {
        _boissons.value = boissonsList // Liste statique des boissons
        // Initialiser les états d'activation à true pour toutes les boissons par défaut
        val initialState = boissonsList.associate { it.nom to true }
        _boissonsActivationState.value = initialState
    }

    // Fonction pour activer/désactiver une boisson
    fun toggleBoissonActivation(boissonNom: String, isActivated: Boolean) {
        // Mettre à jour l'état d'activation pour la boisson
        val updatedState = _boissonsActivationState.value?.toMutableMap() ?: mutableMapOf()
        updatedState[boissonNom] = isActivated
        _boissonsActivationState.value = updatedState
    }

    // Fonction pour récupérer l'état d'activation d'une boisson
    fun getBoissonActivationState(boissonNom: String): Boolean {
        return _boissonsActivationState.value?.get(boissonNom) ?: true // Retourne true par défaut
    }
}
