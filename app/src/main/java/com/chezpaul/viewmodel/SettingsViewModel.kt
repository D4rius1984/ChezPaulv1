package com.chezpaul.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {
    var isDarkTheme = mutableStateOf(false)
    var platsSpeciauxState = mutableStateOf(false)
    var ravigoteNotif = mutableStateOf(false)

    // Vous pouvez ajouter d'autres préférences ou options dans ce ViewModel
    fun resetAll() {
        isDarkTheme.value = false
        platsSpeciauxState.value = false
        ravigoteNotif.value = false
    }
}
