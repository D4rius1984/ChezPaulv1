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

    // Etat d'activation pour chaque plat - changé en Map pour cohérence
    private val _platsActivationState = MutableLiveData<Map<String, Boolean>>()
    val platsActivationState: LiveData<Map<String, Boolean>> get() = _platsActivationState

    init {
        loadPlats() // Charger les plats initiaux
    }

    private fun loadPlats() {
        _plats.value = platsData // Liste statique des plats
        // Initialiser les états d'activation à true pour tous les plats par défaut
        _platsActivationState.value = platsData.associate { it.nom to true }
    }

    // Fonction pour activer/désactiver un plat - corrigée
    fun togglePlatActivation(platNom: String, isActivated: Boolean) {
        val currentState = _platsActivationState.value ?: emptyMap()
        _platsActivationState.value = currentState.toMutableMap().apply {
            this[platNom] = isActivated
        }
    }

    // Fonction pour récupérer l'état d'activation d'un plat
    fun getPlatActivationState(platNom: String): Boolean {
        return _platsActivationState.value?.get(platNom) ?: true // Retourne true par défaut
    }

    // Sauvegarder l'état d'activation des plats
    fun updatePlatActivation(updatedState: Map<String, Boolean>) {
        _platsActivationState.value = updatedState
    }

    // **NOUVELLE MÉTHODE** - Réinitialise tous les plats (pour fin de service)
    fun resetAllPlats() {
        // Remet tous les plats à leur état par défaut (activés)
        _platsActivationState.value = platsData.associate { it.nom to true }
    }
}