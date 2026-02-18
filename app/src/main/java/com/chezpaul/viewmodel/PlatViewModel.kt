package com.chezpaul.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.chezpaul.model.PlatConfig
import com.chezpaul.model.platsData

class PlatViewModel : ViewModel() {

    private val _plats = mutableStateOf<List<PlatConfig>>(emptyList())
    val plats: State<List<PlatConfig>> = _plats

    private val _platsActivationState = mutableStateOf<Map<String, Boolean>>(emptyMap())
    val platsActivationState: State<Map<String, Boolean>> = _platsActivationState

    init {
        loadPlats()
    }

    private fun loadPlats() {
        _plats.value = platsData
        _platsActivationState.value = platsData.associate { it.nom to true }
    }

    fun togglePlatActivation(platNom: String, isActivated: Boolean) {
        _platsActivationState.value = _platsActivationState.value.toMutableMap().apply {
            this[platNom] = isActivated
        }
    }

    fun getPlatActivationState(platNom: String): Boolean {
        return _platsActivationState.value[platNom] ?: true
    }

    fun updatePlatActivation(updatedState: Map<String, Boolean>) {
        _platsActivationState.value = updatedState
    }

    fun resetAllPlats() {
        _platsActivationState.value = platsData.associate { it.nom to true }
    }
}
