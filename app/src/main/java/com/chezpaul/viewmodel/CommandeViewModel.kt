package com.chezpaul.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.chezpaul.data.entities.CommandeWithItems
import com.chezpaul.data.entities.ServiceEntity
import com.chezpaul.data.entities.TopItemResult
import com.chezpaul.data.repository.DataRepository
import com.chezpaul.model.CategorieBoisson
import com.chezpaul.model.Commande
import com.chezpaul.model.MenuConstants
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * ViewModel central gérant les commandes en cours, le CA, l'historique des services
 * et les statistiques. Persiste via Room (commandes actives + historique).
 */
class CommandeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = DataRepository(application)

    private val _commandesList = mutableStateOf<List<Commande>>(emptyList())
    val commandesList = _commandesList

    // Etat pour résultat validation (true = ok, false = erreur)
    val validerCommandeResult = mutableStateOf(true)

    // État du BottomSheet (pour ResumeScreen)
    private val _showBottomSheet = mutableStateOf(false)
    val showBottomSheet: State<Boolean> = _showBottomSheet

    private val _selectedCommande = mutableStateOf<Commande?>(null)
    val selectedCommande: State<Commande?> = _selectedCommande

    private val platsIllimites = MenuConstants.PLATS_ILLIMITES

    // Prix du menu dynamique (mis à jour par SettingsViewModel)
    var menuPrice = mutableStateOf(32.0)

    // --- History (Room) ---
    private val _historyList = mutableStateOf<List<ServiceEntity>>(emptyList())
    val historyList: State<List<ServiceEntity>> = _historyList

    // --- Import CSV ---
    private val _importResult = mutableStateOf<String?>(null)
    val importResult: State<String?> = _importResult

    init {
        observeActiveCommandes()
        observeHistory()
        migrateData()
    }

    private fun observeActiveCommandes() {
        viewModelScope.launch {
            repository.activeCommandes.collectLatest { commandes ->
                _commandesList.value = commandes.sortedBy { it.numeroTable.toIntOrNull() ?: Int.MAX_VALUE }
            }
        }
    }

    private fun observeHistory() {
        viewModelScope.launch {
            repository.allHistoryServices.collectLatest { services ->
                _historyList.value = services
            }
        }
    }

    private fun migrateData() {
        viewModelScope.launch {
            repository.migrateActiveCommandesFromPrefs()
            repository.migrateLegacyHistory()
        }
    }

    fun deleteHistoryItem(service: ServiceEntity) {
        viewModelScope.launch {
            repository.deleteService(service)
        }
    }

    fun updateServiceEntity(service: ServiceEntity) {
        viewModelScope.launch {
            repository.updateService(service)
        }
    }

    // --- Gestion des commandes ---

    fun ajouterCommande(cmd: Commande) {
        viewModelScope.launch {
            repository.upsertActiveCommande(cmd)
        }
    }

    fun modifierCommande(cmd: Commande) {
        val cmdWithTimestamp = cmd.copy(modifiedAt = System.currentTimeMillis())
        viewModelScope.launch {
            repository.upsertActiveCommande(cmdWithTimestamp)
        }
    }

    fun supprimerCommande(cmd: Commande) {
        viewModelScope.launch {
            repository.deleteActiveCommande(cmd)
        }
    }

    /** Vérifie la règle 1 plat = 1 couvert (cervelle/st marcelin illimités). */
    fun verifierPlatsSelonCouverts(cmd: Commande): Boolean {
        val nbCouverts = cmd.nombreCouverts
        val aDesPlats = cmd.plats.isNotEmpty()
        val aDesBoissons = cmd.boissons.isNotEmpty()

        if (!aDesPlats && aDesBoissons) return true

        if (aDesPlats) {
            val totalPlats = cmd.plats
                .filter { it.nom.lowercase() !in platsIllimites }
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

    /** Archive le service en cours dans Room puis efface toutes les commandes. */
    fun resetAllCommandes() {
        val currentCommandes = _commandesList.value
        viewModelScope.launch {
            if (currentCommandes.isNotEmpty()) {
                repository.archiveService(currentCommandes, menuPrice.value)
            }
            repository.deleteAllActiveCommandes()
        }
        validerCommandeResult.value = true
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

    /** Supprime la commande existante et la renvoie pour édition dans CommandeScreen. */
    fun startModification(commande: Commande, onReady: (Commande) -> Unit) {
        deleteCommande(commande)
        onReady(commande)
    }

    // --- Calculs Accueil (ex-AccueilViewModel) ---

    val totalCouverts: Int
        get() = _commandesList.value.sumOf { it.nombreCouverts }

    val boissonsParCategorie: Map<CategorieBoisson, Int>
        get() {
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
        get() = _commandesList.value.count { commande ->
            commande.plats.any { plat ->
                plat.contientRavigote || plat.nom.contains("tête de veau", ignoreCase = true)
            }
        }

    val caPlats: Double
        get() = _commandesList.value
            .filter { !it.isGroupe && it.plats.isNotEmpty() }
            .sumOf {
                val adultes = it.nombreCouverts - it.menusEnfants
                adultes * menuPrice.value + it.menusEnfants * MenuConstants.PRIX_MENU_ENFANT
            }

    val caBoissons: Double
        get() = _commandesList.value
            .filter { !it.isGroupe }
            .flatMap { it.boissons }
            .sumOf { it.quantite * it.prix }

    val caGroupes: Double
        get() = _commandesList.value
            .filter { it.isGroupe && it.prixMenuGroupe != null }
            .sumOf { it.nombreCouverts * (it.prixMenuGroupe ?: 0.0) }

    val caTotal: Double
        get() = caPlats + caBoissons + caGroupes

    // --- Phase 2: Service Details ---

    private val _serviceDetail = mutableStateOf<ServiceEntity?>(null)
    val serviceDetail: State<ServiceEntity?> = _serviceDetail

    private val _serviceCommandes = mutableStateOf<List<CommandeWithItems>>(emptyList())
    val serviceCommandes: State<List<CommandeWithItems>> = _serviceCommandes

    fun loadServiceDetail(serviceId: Long) {
        viewModelScope.launch {
            _serviceDetail.value = repository.getServiceById(serviceId)
            _serviceCommandes.value = repository.getCommandesForServiceOnce(serviceId)
        }
    }

    // --- Phase 2: Statistics ---

    data class StatsData(
        val serviceCount: Int = 0,
        val totalCA: Double = 0.0,
        val averageCA: Double = 0.0,
        val totalCommands: Int = 0,
        val totalCoversAll: Int = 0,
        val topPlats: List<TopItemResult> = emptyList(),
        val topBoissons: List<TopItemResult> = emptyList()
    )

    private val _statsData = mutableStateOf(StatsData())
    val statsData: State<StatsData> = _statsData

    fun loadStats() {
        viewModelScope.launch {
            _statsData.value = StatsData(
                serviceCount = repository.getServiceCount(),
                totalCA = repository.getTotalCA(),
                averageCA = repository.getAverageCA(),
                totalCommands = repository.getTotalCommandCount(),
                totalCoversAll = repository.getTotalCovers(),
                topPlats = repository.getTopPlats(10),
                topBoissons = repository.getTopBoissons(10)
            )
        }
    }

    // --- CSV Export ---

    fun exportCsv(onReady: (Intent) -> Unit) {
        viewModelScope.launch {
            val intent = repository.exportCsvFile(getApplication())
            if (intent != null) {
                onReady(intent)
            }
        }
    }

    // --- PDF Export ---

    fun exportPdf(onReady: (Intent) -> Unit) {
        viewModelScope.launch {
            val intent = repository.exportPdfFile(getApplication())
            if (intent != null) {
                onReady(intent)
            }
        }
    }

    // --- CSV Import ---

    fun importCsv(uri: Uri) {
        viewModelScope.launch {
            val result = repository.importCsvFile(getApplication(), uri)
            _importResult.value = when (result) {
                is DataRepository.ImportResult.Success -> "${result.count} service(s) importé(s) avec succès"
                is DataRepository.ImportResult.Error -> result.message
            }
        }
    }

    fun clearImportResult() {
        _importResult.value = null
    }
}
