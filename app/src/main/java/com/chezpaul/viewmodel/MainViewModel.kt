package com.chezpaul.viewmodel

import androidx.lifecycle.ViewModel
import com.chezpaul.model.Commande

class MainViewModel(
    private val commandeViewModel: CommandeViewModel,
    private val settingsViewModel: SettingsViewModel // Injection du SettingsViewModel
) : ViewModel() {

    // Délégation vers SettingsViewModel pour éviter la redondance
    val isDarkTheme = settingsViewModel.isDarkTheme
    val platsSpeciauxState = settingsViewModel.platsSpeciauxState
    val ravigoteNotif = settingsViewModel.ravigoteNotif

    // Ajouter une commande
    fun ajouterCommande(cmd: Commande) {
        commandeViewModel.ajouterCommande(cmd)
    }

    // Supprimer une commande
    fun supprimerCommande(cmd: Commande) {
        commandeViewModel.supprimerCommande(cmd)
    }

    // Délégation vers SettingsViewModel
    fun resetAllSettings() {
        settingsViewModel.resetAll()
    }
}