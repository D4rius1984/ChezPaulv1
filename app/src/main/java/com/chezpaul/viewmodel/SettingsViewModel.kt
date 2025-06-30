package com.chezpaul.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {
    // Paramètres d'apparence
    var isDarkTheme = mutableStateOf(false)

    // Paramètres du menu
    var platsSpeciauxState = mutableStateOf(false)

    // Paramètres de notifications
    var ravigoteNotif = mutableStateOf(false)

    // État du service
    var isServiceActive = mutableStateOf(true)

    fun resetAll() {
        isDarkTheme.value = false
        platsSpeciauxState.value = false
        ravigoteNotif.value = false
    }

    fun closeService(
        context: Context,
        commandeViewModel: CommandeViewModel? = null,
        platViewModel: PlatViewModel? = null,
        boissonViewModel: BoissonViewModel? = null,
        printerViewModel: PrinterViewModel? = null
    ) {
        try {
            // Réinitialiser l'état du service
            isServiceActive.value = false

            // Réinitialiser toutes les commandes
            commandeViewModel?.let {
                it.commandesList.value = emptyList()
            }

            // Réinitialiser les états des plats
            platViewModel?.let {
                it.resetAllPlats()
            }

            // Réinitialiser les états des boissons
            boissonViewModel?.let {
                it.resetAllBoissons()
            }

            // Réinitialiser l'imprimante
            printerViewModel?.let {
                it.resetPrinter()
            }

            // Réinitialiser les paramètres
            resetAll()

            // Redémarrer le service
            startNewService()

            // Afficher le toast de confirmation
            Toast.makeText(context, "RAZ service OK", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Toast.makeText(context, "Erreur lors de la RAZ: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    fun startNewService() {
        isServiceActive.value = true
    }
}