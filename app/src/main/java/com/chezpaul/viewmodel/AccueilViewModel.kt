package com.chezpaul.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.chezpaul.model.Commande
import com.chezpaul.model.CategorieBoisson

class AccueilViewModel(
    private val commandeViewModel: CommandeViewModel // Injection du CommandeViewModel
) : ViewModel() {

    // Liste des commandes
    val commandesList: State<List<Commande>> = commandeViewModel.commandesList

    // Calcul du total des couverts
    val totalCouverts: Int
        get() = commandesList.value.sumOf { it.nombreCouverts }

    // Calcul des boissons par catégorie
    val boissonsParCategorie: Map<CategorieBoisson, Int>
        get() {
            val categories = listOf(
                CategorieBoisson.APEROS to "Apéros",
                CategorieBoisson.VINS to "Vins",
                CategorieBoisson.DIGESTIFS to "Digestifs",
                CategorieBoisson.BIERES to "Bières",
                CategorieBoisson.SOFTS to "Softs"
            )
            return categories.associate { (catEnum, _) ->
                catEnum to commandesList.value.flatMap { commande ->
                    commande.boissons.filter { it.categorie == catEnum }
                }.sumOf { it.quantite }
            }
        }

    // Calcul du nombre de ravigotes
    val nombreRavigotes: Int
        get() = commandesList.value.count { commande ->
            commande.plats.any { plat ->
                plat.contientRavigote || plat.nom.contains("tête de veau", ignoreCase = true)
            }
        }
}
