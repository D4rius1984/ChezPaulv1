package com.chezpaul.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.chezpaul.model.BoissonConfig
import com.chezpaul.model.boissonsList

class BoissonViewModel : ViewModel() {

    private val _boissons = mutableStateOf<List<BoissonConfig>>(emptyList())
    val boissons: State<List<BoissonConfig>> = _boissons

    private val _boissonsActivationState = mutableStateOf<Map<String, Boolean>>(emptyMap())
    val boissonsActivationState: State<Map<String, Boolean>> = _boissonsActivationState

    init {
        loadBoissons()
    }

    private fun loadBoissons() {
        _boissons.value = boissonsList
        _boissonsActivationState.value = boissonsList.associate { it.nom to true }
    }

    fun toggleBoissonActivation(boissonNom: String, isActivated: Boolean) {
        _boissonsActivationState.value = _boissonsActivationState.value.toMutableMap().apply {
            this[boissonNom] = isActivated
        }
    }

    fun getBoissonActivationState(boissonNom: String): Boolean {
        return _boissonsActivationState.value[boissonNom] ?: true
    }

    fun updateBoissonActivation(updatedState: Map<String, Boolean>) {
        _boissonsActivationState.value = updatedState
    }

    fun resetAllBoissons() {
        _boissonsActivationState.value = boissonsList.associate { it.nom to true }
    }
}
