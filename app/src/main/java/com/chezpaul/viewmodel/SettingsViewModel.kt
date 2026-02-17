package com.chezpaul.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.util.Calendar

class SettingsViewModel : ViewModel() {
    // Paramètres d'apparence
    var isDarkTheme = mutableStateOf(false)

    // Paramètres du menu
    var platsSpeciauxState = mutableStateOf(false)

    // Paramètres de notifications
    var ravigoteNotif = mutableStateOf(false)

    // État du service
    var isServiceActive = mutableStateOf(true)

    // Prix du Menu - Override manuel
    // Valeurs possibles : "AUTO", "FORCE_MIDI", "FORCE_SOIR"
    var menuModeOverride = mutableStateOf("AUTO")

    fun getMenuPrice(): Double {
        return when (menuModeOverride.value) {
            "FORCE_MIDI" -> 24.0
            "FORCE_SOIR" -> 32.0
            else -> { // AUTO : basé sur l'heure et le jour
                val now = Calendar.getInstance()
                val dayOfWeek = now.get(Calendar.DAY_OF_WEEK)
                val hour = now.get(Calendar.HOUR_OF_DAY)

                val isWeekday = dayOfWeek in Calendar.MONDAY..Calendar.FRIDAY
                val isLunchHours = hour in 10..17 // 10:00 à 17:59

                if (isWeekday && isLunchHours) 24.0 else 32.0
            }
        }
    }

    fun getMenuModeLabel(): String {
        return when (menuModeOverride.value) {
            "FORCE_MIDI" -> "Midi (24€)"
            "FORCE_SOIR" -> "Soir/WE (32€)"
            else -> {
                val price = getMenuPrice()
                "Auto → ${price.toInt()}€"
            }
        }
    }

    fun resetAll() {
        isDarkTheme.value = false
        platsSpeciauxState.value = false
        ravigoteNotif.value = false
        menuModeOverride.value = "AUTO"
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

            // Archiver puis réinitialiser toutes les commandes
            commandeViewModel?.resetAllCommandes()

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