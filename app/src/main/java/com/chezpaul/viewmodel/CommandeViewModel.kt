package com.chezpaul.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.chezpaul.data.PersistenceManager
import com.chezpaul.model.CategorieBoisson
import com.chezpaul.model.Commande
import com.chezpaul.data.HistoryItem

class CommandeViewModel(application: Application) : AndroidViewModel(application) {
    private val persistenceManager = PersistenceManager(application.applicationContext)
    private val _commandesList = mutableStateOf<List<Commande>>(emptyList())
    val commandesList = _commandesList

    // Etat pour résultat validation (true = ok, false = erreur)
    val validerCommandeResult = mutableStateOf(true)

    // État du BottomSheet (pour ResumeScreen)
    private val _showBottomSheet = mutableStateOf(false)
    val showBottomSheet: State<Boolean> = _showBottomSheet

    private val _selectedCommande = mutableStateOf<Commande?>(null)
    val selectedCommande: State<Commande?> = _selectedCommande
    
    // Constantes
    private val nomCervelle = "cervelle"
    private val nomStMarcelin = "st marcelin"

    // Prix du menu dynamique (mis à jour par SettingsViewModel)
    var menuPrice = mutableStateOf(32.0)

    private val _historyList = mutableStateOf<List<HistoryItem>>(emptyList())
    val historyList = _historyList

    init {
        loadCommandes()
        loadHistory()
    }

    private fun loadCommandes() {
        _commandesList.value = persistenceManager.loadCommandes()
            .sortedBy { it.numeroTable.toIntOrNull() ?: Int.MAX_VALUE }
    }
    
    fun loadHistory() {
        _historyList.value = persistenceManager.loadHistory().sortedByDescending { it.timestamp }
    }

    private fun saveCommandes() {
        persistenceManager.saveCommandes(_commandesList.value)
    }

    fun deleteHistoryItem(item: HistoryItem) {
        persistenceManager.deleteHistoryItem(item)
        loadHistory()
    }



    // --- Gestion des commandes ---

    fun ajouterCommande(cmd: Commande) {
        _commandesList.value = (_commandesList.value + cmd).sortedBy { it.numeroTable.toIntOrNull() ?: Int.MAX_VALUE }
        saveCommandes()
    }

    fun modifierCommande(cmd: Commande) {
        _commandesList.value = _commandesList.value.map { commande ->
            if (commande.numeroTable == cmd.numeroTable) cmd else commande
        }.sortedBy { it.numeroTable.toIntOrNull() ?: Int.MAX_VALUE }
        saveCommandes()
    }

    fun supprimerCommande(cmd: Commande) {
        _commandesList.value = _commandesList.value.filterNot { it.numeroTable == cmd.numeroTable }
        saveCommandes()
    }

    fun verifierPlatsSelonCouverts(cmd: Commande): Boolean {
        val nbCouverts = cmd.nombreCouverts
        val aDesPlats = cmd.plats.isNotEmpty()
        val aDesBoissons = cmd.boissons.isNotEmpty()

        if (!aDesPlats && aDesBoissons) return true

        if (aDesPlats) {
            val totalPlats = cmd.plats
                .filter { it.nom.lowercase() != nomCervelle && it.nom.lowercase() != nomStMarcelin }
                .sumOf { it.quantite }
            return totalPlats == nbCouverts
        }

        return false
    }

    fun validerCommande(cmd: Commande) {
        val estValide = verifierPlatsSelonCouverts(cmd)
        validerCommandeResult.value = estValide

        if (estValide) {
            val existe = _commandesList.value.any { it.numeroTable == cmd.numeroTable }
            if (existe) {
                modifierCommande(cmd)
            } else {
                ajouterCommande(cmd)
            }
        }
    }

    fun resetAllCommandes() {
        // Archiver le service actuel avant de tout effacer
        if (_commandesList.value.isNotEmpty()) {
            persistenceManager.archiveService(_commandesList.value, menuPrice.value)
            loadHistory() // Recharger l'historique après archivage
        }
        
        _commandesList.value = emptyList()
        validerCommandeResult.value = true
        saveCommandes()
    }

    // --- BottomSheet (ex-ResumeViewModel) ---

    fun toggleBottomSheet(commande: Commande?) {
        _selectedCommande.value = commande
        _showBottomSheet.value = commande != null
    }

    fun deleteCommande(commande: Commande) {
        supprimerCommande(commande)
        _showBottomSheet.value = false
        _selectedCommande.value = null
    }

    fun validateCommande(commande: Commande) {
        validerCommande(commande)
    }

    fun startModification(commande: Commande, onReady: (Commande) -> Unit) {
        deleteCommande(commande)
        onReady(commande)
    }

    // --- Calculs Accueil (ex-AccueilViewModel) ---

    val totalCouverts: Int
        @Composable get() = _commandesList.value.sumOf { it.nombreCouverts }

    val boissonsParCategorie: Map<CategorieBoisson, Int>
        @Composable get() {
            val categories = listOf(
                CategorieBoisson.APEROS,
                CategorieBoisson.VINS_FONTAINE,
                CategorieBoisson.VINS_BOUTEILLES,
                CategorieBoisson.DIGESTIFS,
                CategorieBoisson.BIERES,
                CategorieBoisson.SOFTS
            )
            return categories.associateWith { catEnum ->
                _commandesList.value.flatMap { commande ->
                    commande.boissons.filter { it.categorie == catEnum }
                }.sumOf { it.quantite }
            }
        }

    val nombreRavigotes: Int
        @Composable get() = _commandesList.value.count { commande ->
            commande.plats.any { plat ->
                plat.contientRavigote || plat.nom.contains("tête de veau", ignoreCase = true)
            }
        }

    val caPlats: Double
        @Composable get() = _commandesList.value
            .filter { it.plats.isNotEmpty() }
            .sumOf { it.nombreCouverts * menuPrice.value }

    val caBoissons: Double
        @Composable get() = _commandesList.value
            .flatMap { it.boissons }
            .sumOf { it.quantite * it.prix }

    val caTotal: Double
        @Composable get() = caPlats + caBoissons
}
