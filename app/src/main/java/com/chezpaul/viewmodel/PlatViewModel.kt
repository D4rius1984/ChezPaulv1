package com.chezpaul.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chezpaul.model.PlatConfig
import com.chezpaul.model.platsData

class PlatViewModel : ViewModel() {

    // Liste des plats observée
    private val _plats = MutableLiveData<List<PlatConfig>>()
    val plats: LiveData<List<PlatConfig>> get() = _plats

    // Etat d'activation pour chaque plat
    private val _platsActivationState = MutableLiveData<Map<String, Boolean>>(emptyMap())
    val platsActivationState: LiveData<Map<String, Boolean>> get() = _platsActivationState

    init {
        loadPlats() // Charger les plats initiaux
    }

    private fun loadPlats() {
        _plats.value = platsData // Liste statique des plats
        // Initialiser les états d'activation à true pour tous les plats par défaut
        val initialState = platsData.associate { it.nom to true }
        _platsActivationState.value = initialState
    }

    // Fonction pour activer/désactiver un plat
    fun togglePlatActivation(platNom: String, isActivated: Boolean) {
        // Mettre à jour l'état d'activation pour le plat
        val updatedState = _platsActivationState.value?.toMutableMap() ?: mutableMapOf()
        updatedState[platNom] = isActivated
        _platsActivationState.value = updatedState
    }

    // Fonction pour récupérer l'état d'activation d'un plat
    fun getPlatActivationState(platNom: String): Boolean {
        return _platsActivationState.value?.get(platNom) ?: true // Retourne true par défaut
    }
}
