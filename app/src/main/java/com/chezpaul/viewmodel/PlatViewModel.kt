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

    // Variable pour suivre si la ravigote est nécessaire
    private val _ravigoteVisible = MutableLiveData<Boolean>(false)
    val ravigoteVisible: LiveData<Boolean> get() = _ravigoteVisible

    init {
        loadPlats() // Charger les plats initiaux
    }

    private fun loadPlats() {
        _plats.value = platsData // Liste statique des plats
    }

    // Ajouter un plat
    fun addPlat(plat: PlatConfig) {
        val currentPlats = _plats.value?.toMutableList() ?: mutableListOf()
        currentPlats.add(plat)
        _plats.value = currentPlats
    }

    // Fonction pour vérifier la nécessité de la ravigote
    fun updateRavigoteVisibility(platsSelectionnes: Map<String, Int>) {
        val platsAvecRavigote = platsSelectionnes.keys.any { key ->
            key == "Tdv" || key == "Ldb ravigote"
        }
        _ravigoteVisible.value = platsAvecRavigote
    }

    // Fonction pour filtrer les plats selon isGroupe
    fun loadPlatsFiltrés(isGroupe: Boolean) {
        val filteredPlats = platsData.filter {
            if (isGroupe) it.isGroupe else it.isNonGroupe
        }
        _plats.value = filteredPlats
    }
}
