package com.chezpaul.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.chezpaul.model.Commande
import com.chezpaul.model.CategorieBoisson

class AccueilViewModel(
    private val commandeViewModel: CommandeViewModel // Injection du CommandeViewModel
) : ViewModel() {

    // Liste des commandes - MAINTENANT UTILISÉ
    val commandesList: State<List<Commande>> = commandeViewModel.commandesList

    // Calcul du total des couverts - MAINTENANT UTILISÉ
    val totalCouverts: Int
        @Composable get() = commandesList.value.sumOf { it.nombreCouverts }

    // Calcul des boissons par catégorie - MAINTENANT UTILISÉ
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
                commandesList.value.flatMap { commande ->
                    commande.boissons.filter { it.categorie == catEnum }
                }.sumOf { it.quantite }
            }
        }

    // Calcul du nombre de ravigotes - MAINTENANT UTILISÉ
    val nombreRavigotes: Int
        @Composable get() = commandesList.value.count { commande ->
            commande.plats.any { plat ->
                plat.contientRavigote || plat.nom.contains("tête de veau", ignoreCase = true)
            }
        }

    // Prix du menu par couvert
    companion object {
        const val PRIX_MENU = 32.0
    }

    // CA Plats = couverts qui ont des plats x 32€
    val caPlats: Double
        @Composable get() = commandesList.value
            .filter { it.plats.isNotEmpty() }
            .sumOf { it.nombreCouverts * PRIX_MENU }

    // CA Boissons = somme de (quantité x prix) pour chaque boisson
    val caBoissons: Double
        @Composable get() = commandesList.value
            .flatMap { it.boissons }
            .sumOf { it.quantite * it.prix }

    // CA Total
    val caTotal: Double
        @Composable get() = caPlats + caBoissons
}