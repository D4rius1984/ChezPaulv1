package com.chezpaul.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.chezpaul.model.Commande

class MainViewModel : ViewModel() {
    var commandes = mutableStateListOf<Commande>()
    var isDarkTheme = mutableStateOf(false)
    var platsSpeciauxState = mutableStateOf(false)
    var ravigoteNotif = mutableStateOf(false)

    fun resetAll() {
        commandes.clear()
        platsSpeciauxState.value = false
        ravigoteNotif.value = false
        // tu peux reset d'autres states ici si besoin
    }
}
