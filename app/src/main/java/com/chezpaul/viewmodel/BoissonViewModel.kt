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

    // Fonction pour charger les boissons filtrées selon isGroupe ou isNonGroupe
    fun loadBoissons(isGroupe: Boolean) {
        // Filtrer les boissons selon isGroupe ou isNonGroupe
        val filteredBoissons = if (isGroupe) {
            // Filtrer les boissons spécifiques aux groupes
            boissonsList.filter { it.isGroupe }
        } else {
            // Filtrer les boissons spécifiques aux commandes normales
            boissonsList.filter { it.isNonGroupe }
        }

        _boissons.value = filteredBoissons
    }

    // Ajouter une boisson
    fun addBoisson(boisson: BoissonConfig) {
        val currentBoissons = _boissons.value?.toMutableList() ?: mutableListOf()
        currentBoissons.add(boisson)
        _boissons.value = currentBoissons
    }
}
